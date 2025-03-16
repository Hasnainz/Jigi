import os
from glob import glob
import cv2
from pathlib import Path

current_index = 0


for i in range(1, 10):
    load_path = f'./unicode-pictures-dataset/dataset/ETL{i}/'
    files = os.listdir(load_path)
    for character_hex in files:
        glob_filepath = load_path + character_hex + "/*.png"
        filepaths_for_character = glob(glob_filepath)
        for filepath in filepaths_for_character:
            image = cv2.imread(filepath)
            resized_image = cv2.resize(image, (64, 64))
            save_path = "./unicode-pictures-dataset/modified-dataset/" + character_hex + "/"
            Path(save_path).mkdir(parents=True, exist_ok=True)
            save_path += str(current_index) + ".png"
            current_index += 1
            print(save_path)
            cv2.imwrite(save_path, resized_image)


    
