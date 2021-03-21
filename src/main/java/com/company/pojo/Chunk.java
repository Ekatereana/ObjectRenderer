package com.company.pojo;

import com.company.enums.ChunkType;
import lombok.Data;

@Data
public class Chunk {
    private int length;
    private ChunkType type;
    private byte[] CRC;
    private byte[] content;


    public Chunk(int length, ChunkType type, byte[] CRC, byte[] content) {
        this.length = length;
        this.type = type;
        this.CRC = CRC;
        this.content = content;
    }
}
