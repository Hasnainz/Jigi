# A native Android Japanese dictionary app built using Kotlin and Jetpack Compose. It also contains a TFLite handwriting recognition model built using Python 3 and TensorFlow.


## The ml folder contains the code for generating and training the dataset, the descriptions of each file can be seen as follows.
---
`batch.sbatch`

This runs the run.py file on the university server which uses powerful GPUs to speed up calculation. 

`convertotflite.py`

This converts from a .keras file format into a .tflite format which allows it to be run on a mobile device.

`createlabels.py`

This creates a kotlin file that maps integers 0 to 6511 to their allocated Kanji.

`datasetgenerator.ipynb`

This was used for testing different image editing algorithms as well as generating the loss and accuracy graphs.

`main.py`

This was used to copy files from the original dataset into a usable format.

`remove_unwanted.py`

This is used to remove characters that we do not want to recognise, such as alphanumeric characters.

`resize_all.py`

This is used to generate the entire dataset including modifying images and normalising the amount of data that is in each class.

`run.py`

This contains the code to create and train the neural network.

`testing.ipynb` and `testmodel.ipynb`

This contains the code to test the model after training is completed, the two files exist to test the models on different devices.

---

## The model above is then placed into a custom built android app, the results of which can be seen below.


### The handwriting recognition model in action 
![Alt text](/scs/a.jpg?raw=true "Game Load")
![Alt text](/scs/erabuhandwritten.jpg?raw=true "Game Load")

### The dictionary results page 
![Alt text](/scs/eraburesults.jpg?raw=true "Game Load")

### The settings page for being able to import your own dictionary.
![Alt text](/scs/settings.jpg?raw=true "Game Load")
![Alt text](/scs/settingsimport.jpg?raw=true "Game Load")