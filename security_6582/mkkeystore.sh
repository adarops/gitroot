#!/bin/sh

FILES=`find . -name "*.pk8"`

for FILE in $FILES
do
	FILE_NAME=`echo $FILE | awk -F.pk8 '{print $1}'`
	if [ -f ${FILE_NAME}.pem ]; then
		echo "file ${FILE_NAME}.pem exists"
	else
		openssl pkcs8 -inform DER -nocrypt -in ${FILE} -out ${FILE_NAME}.pem
	fi
	openssl pkcs12 -export -in ${FILE_NAME}.x509.pem -out ${FILE_NAME}.p12 -inkey ${FILE_NAME}.pem -password pass:android -name androiddebugkey
	keytool -importkeystore -deststorepass android -destkeystore ${FILE_NAME}.keystore -srckeystore ${FILE_NAME}.p12 -srcstoretype PKCS12 -srcstorepass android
done

