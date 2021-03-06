package org.usfirst.frc948.NRGRobot2018.vision;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PixyCam {
	// PixyCam constants
	public static final int PIXY_INITIAL_ARRAYSIZE = 30;
	public static final int PIXY_MAXIMUM_ARRAYSIZE = 130;
	public static final int PIXY_START_WORD = 0xaa55;
	public static final int PIXY_START_WORD_CC = 0xaa56;
	public static final int PIXY_START_WORDX = 0x55aa;
	public static final int PIXY_MAX_SIGNATURE = 7;
	public static final int PIXY_DEFAULT_ARGVAL = 0xffff;

	private IPixyLink link;
	private boolean skipStart = false;
	private boolean isEnabled = true;
	private Timer timer = new Timer();
	private BlockType blockType;
	volatile ArrayList<Block> pixyFrameData = new ArrayList<Block>(10); // arraylist to store blocks in each frame

	public PixyCam(IPixyLink link) {
		this.link = link;
	}

	public void startVisionThread() { // starts vision thread
		timer.schedule(new VisionTask(this), 0, 10);
		this.enableVision();
	}

	private boolean getStart() { // checks for new frames
//		System.out.println("getStart");
		int word, prevWord;
		prevWord = 0xffff;

		while (true) {
			word = link.getWord();

			if (word == PIXY_START_WORD && prevWord == PIXY_START_WORD) {
				System.out.println("getStart(): New frame, Normal Block");
				blockType = BlockType.NORMAL_BLOCK;
				return true;
			} else if (word == PIXY_START_WORD_CC && prevWord == PIXY_START_WORD) {
				System.out.println("\ngetStart(): New frame, CC Block");
				blockType = BlockType.CC_BLOCK;
				return true;
			} else if (word == PIXY_START_WORDX) {
				System.out.println("getStart(): resyncing");
				link.getByte(); // resync
			} else if (word == 0 && prevWord == 0) {
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}

			prevWord = word;
		}
	}

	private void updateFrameData() { // parses data into blocks, then updates pixyFrameData when frame ends
		if (this.isVisionEnabled()) {
			ArrayList<Block> blocks = new ArrayList<Block>(PIXY_MAXIMUM_ARRAYSIZE);

			if (!skipStart) {
				if (!getStart()) { // no data
					setPixyFrameData(blocks);
					return;
				}
			} else {
				skipStart = false;
			}

			while (blocks.size() < PIXY_MAXIMUM_ARRAYSIZE) { // maximum number of objects per frame that can be sent by
																// pixy
				int checksum = link.getWord();

				if (checksum == PIXY_START_WORD) { // previous word was extra sync word to indicate new frame
					System.out.println("getBlocksLoop(): New frame, normal block");
					skipStart = true;
					blockType = BlockType.NORMAL_BLOCK;
					break;
				} else if (checksum == PIXY_START_WORD_CC) { // previous word was extra sync word to indicate new frame
					System.out.println("getBlocksLoop(): New frame, color code block");
					skipStart = true;
					blockType = BlockType.CC_BLOCK;
					break;
				} else if (checksum == 0) { // no data
					break;
				}

				Block block = new Block(link, blockType); // constructor reads data from link and stores in fields e.g.
															// x, y

				if (block.getChecksum() == checksum) {
					System.out.print("getBlocksLoop(): Checksums equal, added " + block);
//					System.out.println(", distance in inches = " + distance);
					blocks.add(block);
				} else {
					System.out.println(
							"getBlocksLoop(): Checksums not equal: " + block.getChecksum() + " != " + checksum);
				}

				int word = link.getWord();

				if (word == PIXY_START_WORD) {
					System.out.println("getBlocksLoop(): got normal sync word");
					blockType = BlockType.NORMAL_BLOCK;
				} else if (word == PIXY_START_WORD_CC) {
					System.out.println("getBlocksLoop(): got CC sync word");
					blockType = BlockType.CC_BLOCK;
				} else { // unexpected data
					System.out.println("getBlocksLooop(): unexpected data, w = " + Integer.toHexString(word));
					break;
				}
			}

			setPixyFrameData(blocks);
			System.out.println("getBlocksLoop(): blocks added to list, size: " + blocks.size() + "\n");
		}
	}

	public synchronized ArrayList<Block> getPixyFrameData() { // will be called in other classes to retrieve frame data
		return pixyFrameData;
	}

	private synchronized void setPixyFrameData(ArrayList<Block> blocks) {
		this.pixyFrameData = blocks;
	}

	void setBrightness(int brightness) { // set camera brightness
		byte[] outBuf = new byte[3];

		outBuf[0] = (byte) 0x00;
		outBuf[1] = (byte) 0xfe;
		outBuf[2] = (byte) brightness;

		link.send(outBuf);
	}

	public boolean isVisionEnabled() {
		return isEnabled;
	}

	public void enableVision() {
		isEnabled = true;
	}

	public void disableVision() {
		isEnabled = false;
	}

	enum BlockType {
		NORMAL_BLOCK, CC_BLOCK
	}

	public class Block {
		public int signature, x, y, width, height, angle;
		private int checksum;

		public Block(IPixyLink link, BlockType blockType) {
			signature = link.getWord();
			x = link.getWord();
			y = link.getWord();
			width = link.getWord();
			height = link.getWord();
			angle = (blockType == BlockType.CC_BLOCK) ? link.getWord() : 0; // only CC blocks send angle value

			checksum = signature + x + y + width + height + angle;
		}

		public int getChecksum() {
			return checksum;
		}

		public String toString() {
			return String.format("Block: signature=%04X, x=%d, y=%d, width=%d, height=%d, angle=%d", signature, x, y,
					width, height, angle);
		}
	}

	private class VisionTask extends TimerTask {
		private PixyCam pixyCam;

		public VisionTask(PixyCam pixyCam) {
			this.pixyCam = pixyCam;
		}

		@Override
		public void run() {
			pixyCam.updateFrameData();
		}
	}
}
