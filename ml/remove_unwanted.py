import shutil
import os


path = "unicode-pictures-dataset/modified-dataset"
for directory in os.listdir(path):
    if chr(int(directory, 16)) < 'あ':
        path_to_remove = f"{path}/{directory}/"
        print(f"Removing directory {directory}, {chr(int(directory, 16))} {path_to_remove}")
        # shutil.rmtree(path_to_remove)