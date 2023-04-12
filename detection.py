#
# @author:
# 
# Description: 
# This program uses a TensorFlow Lite model to perform object detection on a live webcam
# feed. It draws boxes and gives confidence around the objects of interest in each frame from the
# webcam.
#
# This code is based off the TensorFlow Lite image classification example at:
# https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/examples/python/label_image.py
#
# Added own method of drawing boxes and labels using OpenCV.

# Import packages
import os
import argparse
import cv2
import numpy as np
import sys
import time
from threading import Thread
import importlib.util

# Define VideoStream class to handle streaming of video from webcam in separate processing thread
# Source - Adrian Rosebrock, PyImageSearch: https://www.pyimagesearch.com/2015/12/28/increasing-raspberry-pi-fps-with-python-and-opencv/
class VideoStream:
    """Camera object that controls video streaming from the Picamera"""
    def __init__(self,resolution=(640,480),framerate=30):
        # Initialize the PiCamera and the camera image stream
        self.stream = cv2.VideoCapture(0)
        ret = self.stream.set(cv2.CAP_PROP_FOURCC, cv2.VideoWriter_fourcc(*'MJPG'))
        ret = self.stream.set(3,resolution[0])
        ret = self.stream.set(4,resolution[1])
            
        # Read first frame from the stream
        (self.grabbed, self.frame) = self.stream.read()

        # Variable to control when the camera is stopped
        self.stopped = False

    def start(self):
        # Start the thread that reads frames from the video stream
        Thread(target=self.update,args=()).start()
        return self

    def update(self):
        # Keep looping indefinitely until the thread is stopped
        while True:
            # If the camera is stopped, stop the thread
            if self.stopped:
                # Close camera resources
                self.stream.release()
                return

            # Otherwise, grab the next frame from the stream
            (self.grabbed, self.frame) = self.stream.read()

    def read(self):
        # Return the most recent frame
        return self.frame

    def stop(self):
        # Indicate that the camera and thread should be stopped
        self.stopped = True

# Define and parse input args
parse = argparse.ArgumentParser()
parse.add_argument('--modeldir', help='Folder .tflite file is located in',
                    required=True)
parse.add_argument('--graph', help='Name of .tflite file, is different than the detect .tflite',
                    default='detect.tflite')
parse.add_argument('--labels', help='Name of labelmap file, is different than the labelmap.txt',
                    default='labelmap.txt')
parse.add_argument('--threshold', help='Minimum confidence threshold for detected objects',
                    default=0.5)
parse.add_argument('--resolution', help='Desired webcam resolution in WxH. If resolution not support, errors may occur.',
                    default='1280x720')
parse.add_argument('--edgetpu', help='Use Coral Edge TPU Accelerator to speed up detection',
                    action='store_true')

args = parse.parse_args()


min_conf_threshold = float(args.threshold)
resW, resH = args.resolution.split('x')
imW, imH = int(resW), int(resH)
use_TPU = args.edgetpu

MODEL_NAME = args.modeldir
GRAPH_NAME = args.graph
LABELMAP_NAME = args.labels

# Import TensorFlow libraries
package = importlib.util.find_spec('tflite_runtime')
if package:
    from tflite_runtime.interpreter import Interpreter
    if use_TPU:
        from tflite_runtime.interpreter import load_delegate
else:
    from tensorflow.lite.python.interpreter import Interpreter
    if use_TPU:
        from tensorflow.lite.python.interpreter import load_delegate

# If using Edge TPU, assign filename for Edge TPU model
if use_TPU:
    # If specified name of .tflite, use that name, otherwise  'edgetpu.tflite'
    if (GRAPH_NAME == 'detect.tflite'):
        GRAPH_NAME = 'edgetpu.tflite'       

# Path to current working directory
CWD_PATH = os.getcwd()

# Path to .tflite
PATH_TO_CKPT = os.path.join(CWD_PATH,MODEL_NAME,GRAPH_NAME)

# Path to label map
PATH_TO_LABELS = os.path.join(CWD_PATH,MODEL_NAME,LABELMAP_NAME)

# Load label map
with open(PATH_TO_LABELS, 'r') as f:
    label_map = [line.strip() for line in f.readlines()]

# https://www.tensorflow.org/lite/models/object_detection/overview
# First label is '???', which has to be removed.
if label_map[0] == '???':
    del(label_map[0])

# Load the Tensorflow Lite model.
# If using Edge TPU, use special load_delegate argument
if use_TPU:
    interpreter = Interpreter(model_path=PATH_TO_CKPT,
                              experimental_delegates=[load_delegate('libedgetpu.so.1.0')])
    print(PATH_TO_CKPT)
else:
    interpreter = Interpreter(model_path=PATH_TO_CKPT)

interpreter.allocate_tensors()

# Get model details
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
height = input_details[0]['shape'][1]
width = input_details[0]['shape'][2]

floating_model = (input_details[0]['dtype'] == np.float32)

mean = 127.5
std = 127.5

# Initialize frame rate calculation
frame_rate_calculation = 1
freq = cv2.getTickFrequency()

# Initialize video stream
videostream = VideoStream(resolution=(imW,imH),framerate=30).start()
time.sleep(1)

num = 0

time1 = time.time()
while True:

    # Start timer (for calculating frame rate)
    t1 = cv2.getTickCount()

    # Grab frame from video stream
    frame1 = videostream.read()

    # Acquire frame and resize to expected shape [1xHxWx3]
    frame = frame1.copy()
    frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    frame_resized = cv2.resize(frame_rgb, (width, height))
    data = np.expand_dims(frame_resized, axis=0)

    # Normalize pixel values
    if floating_model:
        data = (np.float32(data) - mean) / std

    # Detect by running the model with image as input
    interpreter.set_tensor(input_details[0]['index'],data)
    interpreter.invoke()

    # Retrieve detection results
    boxes = interpreter.get_tensor(output_details[0]['index'])[0] # Bounding box coordinates of detected objects
    classes = interpreter.get_tensor(output_details[1]['index'])[0] # Class index of detected objects
    confidence = interpreter.get_tensor(output_details[2]['index'])[0] # Confidence of detected objects

    # Loop over detections and draw box if confidence is above threshold
    for i in range(len(confidence)):
        if ((confidence[i] > min_conf_threshold) and (confidence[i] <= 1.0)):

            # Get bounding box coordinates and draw box
            # Force coordniate to be within image using max() and min()
            y_min = int(max(1,(boxes[i][0] * imH)))
            x_min = int(max(1,(boxes[i][1] * imW)))
            y_max = int(min(imH,(boxes[i][2] * imH)))
            x_max = int(min(imW,(boxes[i][3] * imW)))
            
            cv2.rectangle(frame, (x_min,y_min), (x_max,y_max), (10, 255, 0), 2)

            # Draw label
            object_name = label_map[int(classes[i])] # Get object name
            label = '%s: %d%%' % (object_name, int(confidence[i]*100))
            labelSize, baseLine = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.7, 2) # Get font size
            label_ymin = max(y_min, labelSize[1] + 10)
            cv2.rectangle(frame, (x_min, label_ymin-labelSize[1]-10), (x_min+labelSize[0], label_ymin+baseLine-10), (255, 255, 255), cv2.FILLED) # Draw white box to place text in
            cv2.putText(frame, label, (x_min, label_ymin-7), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 0), 2) # Draw text

    # Place framerate in corner of frame
    cv2.putText(frame,'FPS: {0:.2f}'.format(frame_rate_calculation),(30,50),cv2.FONT_HERSHEY_SIMPLEX,1,(255,255,0),2,cv2.LINE_AA)

    # Display results on frame
    cv2.imshow('Object detector', frame)
    print(str(label_map[int(classes[i])]))

    # Calculate framerate
    t2 = cv2.getTickCount()
    time1 = (t2-t1)/freq
    frame_rate_calculation= 1/time1

    # Exit status
    if cv2.waitKey(1) == ord('e'):
        break
    
    num=+1
    time2 = time.time()
    if ((time2 - time1) >= 1):
        print("time difference : ", time2 - time1)
        print("the code looped : ", count )
        time1 = time.time()


# Clean up
cv2.destroyAllWindows()
videostream.stop()
