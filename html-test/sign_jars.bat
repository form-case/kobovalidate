REM password is 460stephens
REM all of this was figured out from: https://support.comodo.com/index.php?_m=knowledgebase&_a=viewarticle&kbarticleid=1072

jarsigner -storetype pkcs12 -keystore neil.hendrick@berkeley.edu.pfx FormBuilderValidatorApplet.jar "neil hendrick"
jarsigner -storetype pkcs12 -keystore neil.hendrick@berkeley.edu.pfx plugin.jar "neil hendrick"
jarsigner -storetype pkcs12 -keystore neil.hendrick@berkeley.edu.pfx javarosa-libraries.jar "neil hendrick"
jarsigner -storetype pkcs12 -keystore neil.hendrick@berkeley.edu.pfx kxml2-2.3.0.jar "neil hendrick"