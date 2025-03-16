import os

modified_filepath = f'./unicode-pictures-dataset/new-modified-dataset/'
hex_labels = os.listdir(modified_filepath)

temp_hex_labels = sorted([chr(int(x,16)) for x in hex_labels])

with open('label-map.txt', 'w') as f:
    f.write(f"val labelsMap = mapOf(\n")
    for i, label in enumerate(temp_hex_labels):
        f.write(f"{i} to \"{label}\",\n")
    f.write(")")