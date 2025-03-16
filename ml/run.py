import tensorflow as tf
import os



modified_filepath = f'/tmp/dcs-tmp.u2104990/dataset1/new-modified-dataset/'

# print(os.listdir(f'/dcs/large/u2104990/tmp2/'))

hex_labels = os.listdir(modified_filepath)

print(f"Found {len(hex_labels)} files.")

# for label in hex_labels:
#     a = modified_filepath + label + "/"
#     b = len(os.listdir(a))
#     print(f"found {b} files in {a}")

bs=1024
val_split = 0.1
h, w = 64, 64
t_dataset = tf.keras.preprocessing.image_dataset_from_directory(
    modified_filepath,
    labels="inferred",
    label_mode="categorical",
    interpolation="lanczos3",
    color_mode="grayscale",
    batch_size=bs,
    image_size=(h, w),
    shuffle=True,
    seed=291,
    validation_split=val_split,
    subset="validation"
)
# t_dataset = t_dataset.map(lambda x, y : (tf.cast(x, dtype=tf.float16), tf.cast(y, dtype=tf.float16)))

# normalization_layer = tf.keras.layers.Rescaling(1./255)
# t_dataset = t_dataset.map(lambda x, y: (normalization_layer(x), y))
t_dataset.save(path=f'/dcs/large/u2104990/training_data')

print("Finished loading data")
exit()


training = tf.data.Dataset.load('/dcs/large/u2104990/training_data/')

def M1(outnodes):
    return tf.keras.Sequential([

        # tf.keras.layers.InputLayer(shape=(64, 64, 1)),


        tf.keras.applications.EfficientNetV2B0(
            include_top=True,
            include_preprocessing=False,
            weights=None,
            input_shape=(64, 64, 1),
            pooling="avg",
            classes=outnodes,
            classifier_activation='softmax',
        )



        # # tf.keras.layers.Conv2D(filters=9, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.Conv2D(filters=9, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.Conv2D(filters=9, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.MaxPooling2D(pool_size=(2, 2)),

        # tf.keras.layers.Conv2D(filters=9, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.MaxPool2D(pool_size=(2, 2)),
        # tf.keras.layers.Dropout(0.25),

        # tf.keras.layers.Conv2D(filters=9, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.MaxPool2D(pool_size=(2, 2)),
        # tf.keras.layers.Dropout(0.25),

        # tf.keras.layers.Conv2D(filters=3, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.MaxPool2D(pool_size=(2, 2)),
        # tf.keras.layers.Dropout(0.25),

        # tf.keras.layers.Conv2D(filters=3, kernel_size=(3,3), activation='relu'),
        # tf.keras.layers.MaxPool2D(pool_size=(2, 2)),
        # tf.keras.layers.Dropout(0.25),





        # tf.keras.layers.Flatten(),

        # tf.keras.layers.Dense(units=4096, activation= 'relu'),
        # tf.keras.layers.Dropout(0.25),

        # tf.keras.layers.Dense(units=4096, activation= 'relu'),
        # tf.keras.layers.Dropout(0.25),

        # tf.keras.layers.Dense(units=4096, activation= 'relu'),
        # tf.keras.layers.Dropout(0.25),




        # tf.keras.layers.Dense(units=outnodes, activation='softmax')
    ])
    


learning_rate = 0.001
opt = tf.keras.optimizers.Adam(learning_rate=learning_rate)
METRICS = ['accuracy']
model = M1(6512)
model.compile(optimizer=opt, loss='categorical_crossentropy', metrics=METRICS)

print(model.summary())

model.fit(
    training,
    epochs=8,
    verbose=2
)

model.save('my_model.keras')
model.save('my_model.h5')

loss, acc = model.evaluate(training, verbose=2)
print(f"loss: {loss}")
print(f"acc: {acc}")


