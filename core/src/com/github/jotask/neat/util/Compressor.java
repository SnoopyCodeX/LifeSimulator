package com.github.jotask.neat.util;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 * Compressor
 *
 * @author Jose Vives Iznardo
 * @since 20/03/2017
 */
public class Compressor {

    private static final LZ4Factory factory = LZ4Factory.fastestInstance();

    private static int decompressedLength;

    public static byte[] compress(byte[] data){

        decompressedLength = data.length;

        LZ4Compressor compressor = factory.fastCompressor();
        int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
        byte[] compressed = new byte[maxCompressedLength];
        compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);

        return compressed;

    }

    public static byte[] decompress(final byte[] compressedData){
        LZ4FastDecompressor decompressor = factory.fastDecompressor();
        byte[] restored = new byte[decompressedLength];
        decompressor.decompress(compressedData, 0, restored, 0, decompressedLength);
        return restored;
    }

}
