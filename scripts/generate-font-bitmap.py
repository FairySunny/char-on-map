import sys
import cv2 as cv

if len(sys.argv) != 3:
    print(f'Usage: python {sys.argv[0]} <font directory> <output file>')

with open(sys.argv[2], 'wb') as f:
    for idx in range(256):
        img = cv.imread(f'{sys.argv[1]}/unicode_page_{idx:02x}.png', cv.IMREAD_UNCHANGED)
        if img is None:
            f.write(bytes([0]) * (32 * 256))
            print(f'{idx:02x} skipped')
        else:
            for char_row in range(16):
                for char_col in range(16):
                    for pixel_row in range(16):
                        b = 0
                        for pixel_col in range(16):
                            b |= int(img[char_row * 16 + pixel_row, char_col * 16 + pixel_col, 3] >= 128) << pixel_col
                        f.write(bytes([b & 0xff, b >> 8]))
