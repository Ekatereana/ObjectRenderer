package services;

import command.line.parser.instances.ImageInstance;

public interface ConvecterService {
    void convertTo(ImageInstance ii);
    void convertFrom(ImageInstance ii);
}
