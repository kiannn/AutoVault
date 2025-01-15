
package com.example.CarDealerShip.Services;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.springframework.stereotype.Service;

@Service
class CompressionService {

     public byte[] compress(byte[] input) {
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        byte[] compressedData = new byte[input.length];
        int compressedLength = deflater.deflate(compressedData);

        byte[] result = new byte[compressedLength];
        System.arraycopy(compressedData, 0, result, 0, compressedLength);

        return result;
    }



    public byte[] decompress(byte[] data) {
          Inflater inflater = new Inflater();
        inflater.setInput(data);

        byte[] decompressedData = new byte[data.length * 2];
        int decompressedLength;
        try {
            decompressedLength = inflater.inflate(decompressedData);
        } catch (DataFormatException e) {
            return null;
        }

        byte[] result = new byte[decompressedLength];
        System.arraycopy(decompressedData, 0, result, 0, decompressedLength);

        return result;
    }

    
}
