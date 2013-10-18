keytool -genkey -alias koboself -keystore validatorSelfSignedKeystore -validity 365 < selfsignedkeystore.conf
jarsigner -keystore validatorSelfSignedKeystore -storepass 2632StoutSt -keypass 2632StoutSt FormBuilderValidatorApplet.jar koboself
jarsigner -keystore validatorSelfSignedKeystore -storepass 2632StoutSt -keypass 2632StoutSt plugin.jar koboself
jarsigner -keystore validatorSelfSignedKeystore -storepass 2632StoutSt -keypass 2632StoutSt ODK_Validate_v1.6.jar koboself