import os
from glob import glob
import cv2
from pathlib import Path
import random
import sys
import random
import math
import multiprocessing as mp
import gc
import time
import datetime
from typing import Tuple, List
from shutil import copy
from tqdm import tqdm

import tensorflow as tf


import PIL
from PIL import Image as PImage
from PIL import ImageFilter, ImageFont, ImageDraw
import numpy as np
import cv2

import matplotlib.pyplot as plt
from IPython.display import Image

#define a font to show japanese characters in matplotlib figures
import matplotlib.font_manager as fm

import matplotlib.pyplot as plt
from skimage import data, filters, morphology, color
from skimage.color import rgb2gray
from skimage.util import invert

import cv2
# For every folder in modified-dataset, if it has more than 300 entries, delete else add 

def smooth(img):
    imgcpy = img.filter(ImageFilter.SMOOTH)
    return imgcpy

def sharpen(img):
    imgcpy = img.filter(ImageFilter.SHARPEN)
    return imgcpy

def blur(img):
    imgcpy = img.filter(ImageFilter.GaussianBlur(radius=random.uniform(0.5, 1.2)))
    return imgcpy
    
def dilate(img):
    kernel = np.ones((2, 2), np.uint8) 
    imgcpy = cv2.dilate(np.array(img), kernel, iterations=1)  
    return PImage.fromarray(imgcpy)

def erode(img):
    kernel = np.ones((2, 2), np.uint8) 
    imgcpy = cv2.erode(np.array(img), kernel, iterations=2)  
    return PImage.fromarray(imgcpy)

def shear(img):
    y_shear = random.uniform(-0.1, 0.1)
    x_shear = random.uniform(-0.1, 0.1)
    imgcpy = img.transform(img.size, PImage.AFFINE, (1, x_shear, 0, y_shear, 1, 0))
    return imgcpy

def create_image(size, bgColor, message, font, fontColor):
    W, H = size
    image = PImage.new('RGB', size, bgColor)
    draw = ImageDraw.Draw(image)
    _, _, w, h = draw.textbbox((0, 0), message, font=font)
    draw.text(((W-w)/2, (H-h)/2), message, font=font, fill=fontColor)
    return image    

fontsize = 5
a = ImageFont.truetype(font="./fonts/ZenMaruGothic-Regular.ttf", size = fontsize)
b = ImageFont.truetype(font="./fonts/ZenKurenaido-Regular.ttf", size = fontsize)
c = ImageFont.truetype(font="./fonts/HachiMaruPop-Regular.ttf", size = fontsize)
d = ImageFont.truetype(font="./fonts/YujiBoku-Regular.ttf", size = fontsize)
e = ImageFont.truetype(font="./fonts/NotoSerifJP-VariableFont_wght.ttf", size = fontsize)


kanji = "鬱"

# W, H = 64, 64
# image = PImage.new('RGB', (W, H), (255, 255, 255))
# draw = ImageDraw.Draw(image)
# _, _, w, h = draw.textbbox((0, 0), kanji, font=mincho)
# draw.text(((W-w)/2, (H-h)/2), kanji, font=mincho, fill=(0,0,0))

img_fraction = 0.51

image = create_image((64 ,64), 'white', kanji, a, 'black')
while a.getbbox(kanji)[2] < img_fraction * image.size[0]:
    fontsize += 1
    a = ImageFont.truetype(font="./fonts/ZenMaruGothic-Regular.ttf", size = fontsize)



fontsize = 5
image = create_image((64 ,64), 'white', kanji, b, 'black')
while b.getbbox(kanji)[2] < img_fraction * image.size[0]:
    fontsize += 1
    b = ImageFont.truetype(font="./fonts/ZenKurenaido-Regular.ttf", size = fontsize)



image = create_image((64 ,64), 'white', kanji, c, 'black')
while c.getbbox(kanji)[2] < img_fraction * image.size[0]:
    fontsize += 1
    c = ImageFont.truetype(font="./fonts/HachiMaruPop-Regular.ttf", size = fontsize)



fontsize = 5
image = create_image((64 ,64), 'white', kanji, d, 'black')
while d.getbbox(kanji)[2] < img_fraction * image.size[0]:
    fontsize += 1
    d = ImageFont.truetype(font="./fonts/YujiBoku-Regular.ttf", size = fontsize)



fontsize = 5
image = create_image((64 ,64), 'white', kanji, e, 'black')
while e.getbbox(kanji)[2] < img_fraction * image.size[0]:
    fontsize += 1
    e = ImageFont.truetype(font="./fonts/NotoSerifJP-VariableFont_wght.ttf", size = fontsize)


fonts = [a,b,c,d,e]


def threshhold(img: Image):
    img = convert_from_image_to_cv2(img)

    _, image_black = cv2.threshold(img, 154, 255, cv2.THRESH_BINARY)

    return convert_from_cv2_to_image(image_black)

def convert_from_cv2_to_image(img: np.ndarray) -> Image:
    # return Image.fromarray(img)
    return PImage.fromarray(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))


def convert_from_image_to_cv2(img: Image) -> np.ndarray:
    # return np.asarray(img)
    return cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

# def random_translation(img: Image):
#     a = convert_from_image_to_cv2(img)
#     b = tf.keras.preprocessing.image.random_shift(
#         a,
#         wrg=0.1,
#         hrg=0.1,
#         row_axis=0,
#         col_axis=1,
#         channel_axis=2,
#         fill_mode="nearest",
#         cval=0.0,
#         interpolation_order=1,
#     )
#     return convert_from_cv2_to_image(b)


    

# def random_zoom_center(img, amount = 0.6):
#     zoom_factor = 1 + amount * random.random()
#     a = convert_from_image_to_cv2(img)

#     y_size = a.shape[0]
#     x_size = a.shape[1]
    
#     # define new boundaries
#     x1 = int(0.5*x_size*(1-1/zoom_factor))
#     x2 = int(x_size-0.5*x_size*(1-1/zoom_factor))
#     y1 = int(0.5*y_size*(1-1/zoom_factor))
#     y2 = int(y_size-0.5*y_size*(1-1/zoom_factor))

#     # first crop image then scale
#     img_cropped = a[y1:y2,x1:x2]
#     b = cv2.resize(img_cropped, None, fx=zoom_factor, fy=zoom_factor)
#     return convert_from_cv2_to_image(b)
    
# def random_erasing(img: Image, amount=0.2):
#     a = convert_from_image_to_cv2(img)
#     width, length, channels = a.shape
#     for i in range(width):
#         for j in range(length):
#             if a[i, j, 0] != 255 and random.random() < amount:
#                 a[i, j, 0] = 255
#                 a[i, j, 1] = 255
#                 a[i, j, 2] = 255

#     return convert_from_cv2_to_image(a)

def random_zoom_center(img, amount = 0.6):
    a = img
    zoom_factor = 1 + amount * random.random()

    y_size = a.shape[0]
    x_size = a.shape[1]
    
    # define new boundaries
    x1 = int(0.5*x_size*(1-1/zoom_factor))
    x2 = int(x_size-0.5*x_size*(1-1/zoom_factor))
    y1 = int(0.5*y_size*(1-1/zoom_factor))
    y2 = int(y_size-0.5*y_size*(1-1/zoom_factor))

    # first crop image then scale
    img_cropped = a[y1:y2,x1:x2]
    b = cv2.resize(img_cropped, None, fx=zoom_factor, fy=zoom_factor)
    return b


def random_erasing(img, amount = 0.5):
    image = img.astype(np.uint8) * 255
    w, h = image.shape
    for i in range(w):
        for j in range(h):
            if image[i][j] != 0 and random.random() < amount:
                image[i][j] = math.floor(255 * random.random())
    return invert(image)

def random_translation(img: Image):
    a = img
    b = tf.keras.preprocessing.image.random_shift(
        a,
        wrg=0.1,
        hrg=0.1,
        row_axis=0,
        col_axis=1,
        channel_axis=2,
        fill_mode="nearest",
        cval=0.0,
        interpolation_order=1,
    )
    return b
    

def rotate(image):
    angle = math.floor(random.random() * 25)
    if random.random() < 0.5:
        angle *= -1
    (h, w) = image.shape[:2]
    (cX, cY) = (w // 2, h // 2)
    M = cv2.getRotationMatrix2D((cX, cY), angle, 1.0)
    cos = np.abs(M[0, 0])
    sin = np.abs(M[0, 1])
    # compute the new bounding dimensions of the image
    nW = int((h * sin) + (w * cos))
    nH = int((h * cos) + (w * sin))
    # adjust the rotation matrix to take into account translation
    M[0, 2] += (nW / 2) - cX
    M[1, 2] += (nH / 2) - cY
    # perform the actual rotation and return the image
    return cv2.warpAffine(image, M, (nW, nH), borderMode=cv2.BORDER_CONSTANT, borderValue=(255,255,255))

def preprocess(img):
    img = random_zoom_center(img)
    img = random_translation(img)
    img = rotate(img)
    img = rgb2gray(img)
    img = img < filters.threshold_otsu(img)
    img = morphology.skeletonize(img)
    img = random_erasing(img)
    return img

def preprocess_font(img):
    img = random_translation(img)
    img = rgb2gray(img)
    img = img < filters.threshold_otsu(img)
    img = morphology.skeletonize(img)
    img = random_erasing(img)
    return img

Path("./unicode-pictures-dataset/new-modified-dataset/").mkdir(parents=True, exist_ok=True)

labels = None
with open('labels.txt', 'r') as t:
    labels = t.read()
    

modified_filepath = f'./unicode-pictures-dataset/modified-dataset/'
existing_labels = os.listdir(modified_filepath)
existing_labels.sort()


encoded_labels = [hex(ord(x)) for x in labels]
encoded_labels.sort()

all_labels = list(set(encoded_labels + existing_labels))
all_labels.sort()


for i, label in enumerate(all_labels):
    kanji = chr(int(label, 16))
    save_path = "./unicode-pictures-dataset/modified-dataset/" + label + "/"
    Path(save_path).mkdir(parents=True, exist_ok=True)
    print(f"creating directory for {kanji} if doesn't already exist - progress -> {i}/{len(all_labels)}")

modified_filepath = f'./unicode-pictures-dataset/modified-dataset/'
hex_labels = os.listdir(modified_filepath)
file_limit = 1000
current_index = 0
for i, label in enumerate(hex_labels):
    print(f"Progress -> {i+1}/{len(hex_labels)}")
    kanji = chr(int(label, 16))
    current_path = modified_filepath + label + "/"
    save_path = "./unicode-pictures-dataset/new-modified-dataset/" + label + "/"
    Path(save_path).mkdir(parents=True, exist_ok=True)
    files_in_current_path = os.listdir(current_path)
    x = len(files_in_current_path)
    if x >= file_limit:
        print(f"removing from character: {kanji} as there are {x} entries")
        # copy 500 random files to the new directory
        random_sample = random.sample(files_in_current_path, file_limit)
        for sample in random_sample:
            frome = current_path + sample
            two = save_path + str(current_index) + ".png"
            # Here I can edit the image file with 
            original = cv2.imread(frome)
            # res = ((original - original.min()) * 255.0 / (original.max() - original.min())).astype(np.uint8)
            # converted = shear(sharpen(random_translation(random_zoom_center(random_erasing(threshhold(res), amount=0.25)))))
            cv2.imwrite(two, preprocess(original))
            # print(f"moving file {frome} to {two}")
            # copy(frome, two)
            current_index += 1 
    else:  
        number_of_entries_to_add = file_limit - x
        print(f"adding {number_of_entries_to_add} characters to character: {kanji} as there are only {x} entries")
        # copy all existing files and then we generate new files
        for file in files_in_current_path:
            frome = current_path + file
            two = save_path + str(current_index) + ".png"
            # print(f"moving file {frome} to {two}")
            original = cv2.imread(frome)
            # res = ((original - original.min()) * 255.0 / (original.max() - original.min())).astype(np.uint8)
            # converted = shear(sharpen(random_translation(random_zoom_center(random_erasing(threshhold(res), amount=0.25)))))
            cv2.imwrite(two, convert_from_image_to_cv2(preprocess(original)))
            # cv2.imwrite(two, convert_from_image_to_cv2(converted))
            current_index += 1 

        print(f"creating data for {kanji}")
        images = []

        for i in range(number_of_entries_to_add):
            font = fonts[i % len(fonts)]
            # image = PImage.new('RGB', (64, 64), (255, 255, 255))
            # draw = ImageDraw.Draw(image)
            # draw.text(( 3, 3 ), kanji, font=font, fill=(0, 0, 0))    
            image = create_image((64 ,64), 'white', kanji, font, 'black')
            original = convert_from_image_to_cv2(image)
            images.append(convert_from_image_to_cv2(preprocess_font(original)))
            # images.append((shear(sharpen(random_translation(random_zoom_center(random_erasing(image), amount=0.4))))))

        for i, image in enumerate(images):
            two = save_path + str(current_index) + ".png"
            current_index += 1
            # print(f'adding image to {two}')
            cv2.imwrite(two, image)

        
