package com.logic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ByteUtils
{
	/**
	 * Creates and returns a new array with the values of the original from index <code>a</code> to index <code>b</code>
	 * and of size <code>(b-a)</code>.
	 * @param in input array
	 * @param a first index
	 * @param b last index
	 * @return a new array based on the desired range of the input
	 */
	public static byte[] subarray(byte[] in, int a, int b)
	{
		if (b - a > in.length) return in;

		byte[] out = new byte[(b - a) + 1];

		for (int i = a; i <= b; i++)
		{
			out[i - a] = in[i];
		}
		return out;
	}
	
	/**
	 * Functions similarly to the standard java <code>String.trim()</code> method (except that null bytes (0x00),
	 * instead of whitespace, are stripped from the beginning and end). If the input array alread has no leading/trailing null bytes,
	 * is returned unmodified.
	 * 
	 * @param arr the input array
	 * @return an array without any leading or trailing null bytes
	 */
	public static byte[] trim(byte[] arr)
	{
		 //return the input if it has no leading/trailing null bytes
		if(arr[0]!=0 && arr[arr.length]!=0) return arr;
		
		int begin=0, end=arr.length;

		 // find the first non-null byte
		for(int i=0; i<arr.length; i++) 
		{
			if(arr[i] != 0) {
				begin = i;
				break;
			}
		}
		
		//find the last non-null byte
		for(int i=arr.length-1; i>=0; i--)
		{
			if(arr[i] != 0) {
				end = i;
				break;
			}
		}
		
		return subarray(arr, begin, end);
	}
	
	/**
	 * Spits the input array into separate byte arrays. Works similarly to <code>String.split()</code>, but always splits on a null byte (0x00).
	 * @param input the input array
	 * @return a new array of byte arrays
	 */
	public static byte[][] split(byte[] input)
	{
		ArrayList<byte[]> temp = new ArrayList<byte[]>();
		
		byte[][] output;
		//excessively large, but this is the maximum size it can be (actually, less, but it's a good upper bound)
		output = new byte[input.length][input.length];
		int index_cache = 0;
		for(int i=0; i<input.length; i++)
		{
			if(input[i] == 0x00)
			{
				byte[] b = subarray(input, index_cache, i-1);
				temp.add(b);
				//note, this is the index *after* the null byte
				index_cache = i+1;
			}
		}
		//get the remaining part
		//prevent duplication if there are no null bytes
		if(index_cache != 0)
		{
			byte[] b = subarray(input, index_cache, input.length-1);
			temp.add(b);
		}
		
		output = new byte[temp.size()][input.length];
		for(int i=0; i<temp.size(); i++)
		{
			output[i] = temp.get(i);
		}
		
		return output;
	}
	
	/**
	 * Creates an new array of length <code>arr+amount</code>, identical to the original, <code>arr</code>,
	 * except with <code>amount</code> null bytes (0x00) padding the end.
	 * @param arr the input array
	 * @param amount the amount of byte to pad
	 * @return a new array, identical to the original, with the desired padding
	 */
	public static byte[] padArrayEnd(byte[] arr, int amount)
	{
		byte[] arr2 = new byte[arr.length+amount];
		for(int i=0; i<arr.length; i++) {
			arr2[i] = arr[i];
		}
		for(int i=arr.length; i<arr2.length; i++) {
			arr2[i] = 0;
		}
		return arr2;
	}
	
	public static short bytesToShort(byte[] b)
	{
		ByteBuffer buf = ByteBuffer.wrap(b, 0, 2);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		return buf.getShort();
	}
	
	//Big endian !!
	public static byte[] intToBytes(int in)
	{
		byte[] b;
		b=new byte[]
		{
			(byte) (in >>> 24	& 0xFF),
			(byte) (in >>> 16	& 0xFF),
			(byte) (in >>> 8	& 0xFF),
			(byte) (in >>> 0	& 0xFF)
		};
		return b;
	}
	
	public static int bytesToInt(byte[] in)
	{
		//note: big-endian by default
		return ByteBuffer.wrap(in).getInt();
	}
	
}
