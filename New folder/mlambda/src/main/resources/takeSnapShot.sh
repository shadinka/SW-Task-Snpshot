#!/bin/sh

FILE_NAME=$(date -u +%s)".png"
echo $FILE_NAME


#Verify Xwindow is opened in Ubuntu, This may fail due to already opened instance
Xvfb :99 -ac &

#Connects the Display with XWindow object
export DISPLAY=:99

#Command line utility that opens the browser and takes snapshot
# Need to add utility path and as parameters, the URL and file name

/home/ubuntu/wkhtmltox/bin/wkhtmltoimage $1 $FILE_NAME

#Copy generated snapshot to S3 bucket
aws s3 cp $FILE_NAME s3://webpagess

#Generate external Web link for the file with experation time of 6000 second.
aws s3 presign s3://webpagess/$FILE_NAME --expires-in 6000