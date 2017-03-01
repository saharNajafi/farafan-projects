#!/bin/bash

#jvm 1    | [INFO] Failed to parse Maven artifact /opt/apache-archiva-2.2.1/./repositories/internal/com/gam/ems-externals/matiran-ims-stub/1.0.0/matiran-ims-stub-1.0.0-sources.jar due to zip file is empty
#jvm 1    | [INFO] Failed to parse Maven artifact /opt/apache-archiva-2.2.1/./repositories/internal/oracle/jdeveloper/gamtools/1.2/gamtools-1.2-sources.jar due to zip file is empty
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/5.0.0/gam-commons-core-5.0.0.jar \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/5.0.0/gam-commons-core-5.0.0.pom \
-Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/5.0.0/gam-commons-core-5.0.0-sources.jar \

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.3/gam-commons-security-1.0.3.jar \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.3/gam-commons-security-1.0.3.pom \
-Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.3/gam-commons-security-1.0.3-sources.jar
#distributionManagement
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/3.5.1/gam-commons-core-3.5.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/3.5.1/gam-commons-core-3.5.1.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/3.5.1/gam-commons-core-3.5.1-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/4.4.11/gam-commons-core-4.4.11.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/4.4.11/gam-commons-core-4.4.11.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gam-parent/1.0/gam-parent-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gam-parent/1.0/gam-parent-1.0.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/1.0/cbk-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/1.0/cbk-1.0.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-commons/1.0/cbk-commons-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-commons/1.0/cbk-commons-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-ear/1.0/cbk-ear-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-ear/1.0/cbk-ear-1.0.ear
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-ejb/1.0/cbk-ejb-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-ejb/1.0/cbk-ejb-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-web/1.0/cbk-web-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/cbk/cbk-web/1.0/cbk-web-1.0.war
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/1.0.0/commons-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/1.0.0/commons-1.0.0.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.0.0/gam-commons-autocomplete-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.0.0/gam-commons-autocomplete-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.0.0/gam-commons-autocomplete-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.1.0/gam-commons-autocomplete-1.1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.1.0/gam-commons-autocomplete-1.1.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.1.0/gam-commons-autocomplete-1.1.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.1.1/gam-commons-autocomplete-1.1.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-autocomplete/1.1.1/gam-commons-autocomplete-1.1.1.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-conversion/1.0.0/gam-commons-conversion-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-conversion/1.0.0/gam-commons-conversion-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-conversion/1.0.0/gam-commons-conversion-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/1.0.1/gam-commons-core-1.0.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/1.0.1/gam-commons-core-1.0.1.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/1.0.1/gam-commons-core-1.0.1-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/2.0.0/gam-commons-core-2.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/2.0.0/gam-commons-core-2.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-core/2.0.0/gam-commons-core-2.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-date/1.0.0/gam-commons-date-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-date/1.0.0/gam-commons-date-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-date/1.0.0/gam-commons-date-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-integration/1.0.0/gam-commons-integration-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-integration/1.0.0/gam-commons-integration-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-integration/1.0.0/gam-commons-integration-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.0/gam-commons-listreader-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.0/gam-commons-listreader-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.0/gam-commons-listreader-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.1/gam-commons-listreader-1.0.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.1/gam-commons-listreader-1.0.1.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.1/gam-commons-listreader-1.0.1-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.5/gam-commons-listreader-1.0.5.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-listreader/1.0.5/gam-commons-listreader-1.0.5.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-mail/1.0.0/gam-commons-mail-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-mail/1.0.0/gam-commons-mail-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-mail/1.0.0/gam-commons-mail-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-profile/1.0.0/gam-commons-profile-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-profile/1.0.0/gam-commons-profile-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-profile/1.0.0/gam-commons-profile-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-scheduler/1.0.0/gam-commons-scheduler-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-scheduler/1.0.0/gam-commons-scheduler-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-scheduler/1.0.0/gam-commons-scheduler-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.0/gam-commons-security-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.0/gam-commons-security-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.0/gam-commons-security-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.1/gam-commons-security-1.0.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.1/gam-commons-security-1.0.1.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.2/gam-commons-security-1.0.2.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/1.0.2/gam-commons-security-1.0.2.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/2.1.0/gam-commons-security-2.1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-security/2.1.0/gam-commons-security-2.1.0.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-stateprovider/1.0.0/gam-commons-stateprovider-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-stateprovider/1.0.0/gam-commons-stateprovider-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-stateprovider/1.0.0/gam-commons-stateprovider-1.0.0-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-stateprovider/2.8.7/gam-commons-stateprovider-2.8.7.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-stateprovider/2.8.7/gam-commons-stateprovider-2.8.7.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-upload/1.0.0/gam-commons-upload-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-upload/1.0.0/gam-commons-upload-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-upload/1.0.0/gam-commons-upload-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-vlp/1.0.0/gam-commons-vlp-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-vlp/1.0.0/gam-commons-vlp-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/commons/gam-commons-vlp/1.0.0/gam-commons-vlp-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/displaytag/1.0/displaytag-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/displaytag/1.0/displaytag-1.0.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0/matiran-ims-stub-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0/matiran-ims-stub-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0/matiran-ims-stub-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/fonts/1.0.0/fonts-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/fonts/1.0.0/fonts-1.0.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamcasclient/1.0/gamcasclient-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamcasclient/1.0/gamcasclient-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamcasclient/2.0/gamcasclient-2.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamcasclient/2.0/gamcasclient-2.0.pom
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.0/gamtools-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.0/gamtools-1.0.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.2/gamtools-1.2.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.2/gamtools-1.2.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.2/gamtools-1.2-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.4/gamtools-1.4.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.4/gamtools-1.4.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.5.1/gamtools-1.5.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.5.1/gamtools-1.5.1.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.5.2/gamtools-1.5.2.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.5.2/gamtools-1.5.2.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.5/gamtools-1.5.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/gamtools/1.5/gamtools-1.5.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr-utils/1.0.0/nocr-utils-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr-utils/1.0.0/nocr-utils-1.0.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr-utils/2.0.0/nocr-utils-2.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr-utils/2.0.0/nocr-utils-2.0.0.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/1.0.0/ems-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/1.0.0/ems-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/1.0.0/ems-1.0.0-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/1.0.2/ems-1.0.2.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/1.0.2/ems-1.0.2.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/1.0.2/ems-1.0.2-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/2.0.0/ems-2.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/2.0.0/ems-2.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/2.0.0/ems-2.0.0-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-commons/2.1.17/ems-commons-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-commons/2.1.17/ems-commons-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-commons/2.1.17/ems-commons-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-dao-api/2.1.17/ems-dao-api-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-dao-api/2.1.17/ems-dao-api-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-dao-api/2.1.17/ems-dao-api-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-dao-jpa-impl/2.1.17/ems-dao-jpa-impl-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-dao-jpa-impl/2.1.17/ems-dao-jpa-impl-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-dao-jpa-impl/2.1.17/ems-dao-jpa-impl-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-ear/2.1.17/ems-ear-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-ear/2.1.17/ems-ear-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-ear/2.1.17/ems-ear-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-api/2.1.17/ems-services-api-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-api/2.1.17/ems-services-api-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-api/2.1.17/ems-services-api-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.1.17/ems-services-ejb-impl-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.1.17/ems-services-ejb-impl-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.1.17/ems-services-ejb-impl-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.3.7/ems-services-ejb-impl-2.3.7.pom.lastUpdated -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.3.7/ems-services-ejb-impl-2.3.7.jar.lastUpdated -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.3.7/ems-services-ejb-impl-2.3.7-sources.jar.lastUpdated
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-subsystems/2.0.0/ems-subsystems-2.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-subsystems/2.0.0/ems-subsystems-2.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-subsystems/2.0.0/ems-subsystems-2.0.0-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-web/2.1.17/ems-web-2.1.17.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-web/2.1.17/ems-web-2.1.17.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-web/2.1.17/ems-web-2.1.17-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-ccos-ear/2.0.0.16/ems-ccos-ear-2.0.0.16.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-ccos-ear/2.0.0.16/ems-ccos-ear-2.0.0.16.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-ccos-ear/2.0.0.16/ems-ccos-ear-2.0.0.16-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-ccos-web/2.0.0.16/ems-ccos-web-2.0.0.16.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-ccos-web/2.0.0.16/ems-ccos-web-2.0.0.16.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-ccos-web/2.0.0.16/ems-ccos-web-2.0.0.16-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-mes-ear/1.0.1.5/ems-mes-ear-1.0.1.5.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-mes-ear/1.0.1.5/ems-mes-ear-1.0.1.5.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-mes-ear/1.0.1.5/ems-mes-ear-1.0.1.5-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-mes-web/1.0.1.5/ems-mes-web-1.0.1.5.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-mes-web/1.0.1.5/ems-mes-web-1.0.1.5.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-mes-web/1.0.1.5/ems-mes-web-1.0.1.5-sources.jar
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-software/1.0.0/ems-software-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-software/1.0.0/ems-software-1.0.0.jar -Dsources=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/subsystems/ems-software/1.0.0/ems-software-1.0.0-sources.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/cglib/2.0.2/cglib-2.0.2.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/cglib/2.0.2/cglib-2.0.2.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-beanutils/1.0/commons-beanutils-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-beanutils/1.0/commons-beanutils-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-collections/1.0/commons-collections-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-collections/1.0/commons-collections-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-configuration/1.0/commons-configuration-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-configuration/1.0/commons-configuration-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-digester/1.8/commons-digester-1.8.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-digester/1.8/commons-digester-1.8.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-fileupload/1.0/commons-fileupload-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-fileupload/1.0/commons-fileupload-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-lang/1.0/commons-lang-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-lang/1.0/commons-lang-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-logging/1.0/commons-logging-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/commons-logging/1.0/commons-logging-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/displaytag/1.0/displaytag-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/displaytag/1.0/displaytag-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/dom4j/1.0/dom4j-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/dom4j/1.0/dom4j-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/ehcache/1.2.4/ehcache-1.2.4.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/ehcache/1.2.4/ehcache-1.2.4.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/ejb/1.0/ejb-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/ejb/1.0/ejb-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/gam.tools/1.0/gam.tools-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/gam.tools/1.0/gam.tools-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/hibernate2/1.0/hibernate2-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/hibernate2/1.0/hibernate2-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/infrastructure/1.0/infrastructure-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/infrastructure/1.0/infrastructure-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/integration/1.0/integration-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/integration/1.0/integration-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jdom/1.0/jdom-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jdom/1.0/jdom-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jdt-compiler/3.1.1/jdt-compiler-3.1.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jdt-compiler/3.1.1/jdt-compiler-3.1.1.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/json/1.0/json-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/json/1.0/json-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jstl/1.0/jstl-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jstl/1.0/jstl-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jta/1.0/jta-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/jta/1.0/jta-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/listreader/1.0.1/listreader-1.0.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/listreader/1.0.1/listreader-1.0.1.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/listreader/1.0/listreader-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/listreader/1.0/listreader-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/listreader/1.1/listreader-1.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/listreader/1.1/listreader-1.1.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/odmg/3.0/odmg-3.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/odmg/3.0/odmg-3.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/ojdbc14dms/1.0/ojdbc14dms-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/ojdbc14dms/1.0/ojdbc14dms-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/orm/1.0/orm-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/orm/1.0/orm-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/relational-xquery/1.0/relational-xquery-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/relational-xquery/1.0/relational-xquery-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/servlet/1.0/servlet-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/servlet/1.0/servlet-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/standard/1.0/standard-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/standard/1.0/standard-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/struts/1.0/struts-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/struts/1.0/struts-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/velocity-dep/1.4/velocity-dep-1.4.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/velocity-dep/1.4/velocity-dep-1.4.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/xalan/1.0/xalan-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/xalan/1.0/xalan-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/xercesImpl/1.0/xercesImpl-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/xercesImpl/1.0/xercesImpl-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/xml-apis/1.0/xml-apis-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/oalib/xml-apis/1.0/xml-apis-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/orm/1.0.0/orm-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/orm/1.0.0/orm-1.0.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/simplecaptcha/1.2.1/simplecaptcha-1.2.1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/simplecaptcha/1.2.1/simplecaptcha-1.2.1.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/tools/1.0/tools-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/tools/1.0/tools-1.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/wlclient/1.0.0/wlclient-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/wlclient/1.0.0/wlclient-1.0.0.jar
mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/gam-parent/gam-parent/1.0/gam-parent-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/gam-parent/gam-parent/1.0/gam-parent-1.0.pom
#mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ -DpomFile=/home/hamidh/projects/css/m2/repository/oracle/jdeveloper/gamtools/1.2/gamtools-1.2.pom -Dfile=/home/hamidh/projects/css/m2/repository/oracle/jdeveloper/gamtools/1.2/gamtools-1.2.jar -Dsources=/home/hamidh/projects/css/m2/repository/oracle/jdeveloper/gamtools/1.2/gamtools-1.2-sources.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/tangosol-coherence/coherence/3.7.1.0/coherence-3.7.1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/tangosol-coherence/coherence/3.7.1.0/coherence-3.7.1.0.jar



mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/iaik-jce/1.0/iaik-jce-1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/iaik-jce/1.0/iaik-jce-1.0.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/iaik-xsect/1.0/iaik-xsect-1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/iaik-xsect/1.0/iaik-xsect-1.0.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/pkcs11-provider/1.3/pkcs11-provider-1.3.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/pkcs11-provider/1.3/pkcs11-provider-1.3.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/pkcs11-wrapper/1.2.18/pkcs11-wrapper-1.2.18.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/at/tugraz/iaik/pkcs11-wrapper/1.2.18/pkcs11-wrapper-1.2.18.jar



mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/tangosol-coherence/coherence-hibernate/3.7.1.0/coherence-hibernate-3.7.1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/tangosol-coherence/coherence-hibernate/3.7.1.0/coherence-hibernate-3.7.1.0.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/oracle/ojdbc6/11.1.0/ojdbc6-11.1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/com/oracle/ojdbc6/11.1.0/ojdbc6-11.1.0.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/oracle/ojdbc6/11.2.0/ojdbc6-11.2.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/com/oracle/ojdbc6/11.2.0/ojdbc6-11.2.0.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=.pom \
-Dfile=.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/org/apache/maven/plugins/maven-surefire-plugin/2.12.4/maven-surefire-plugin-2.12.4.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/org/apache/maven/plugins/maven-surefire-plugin/2.12.4/maven-surefire-plugin-2.12.4.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/org/apache/maven/plugins/maven-surefire-plugin/2.12/maven-surefire-plugin-2.12.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/org/apache/maven/plugins/maven-surefire-plugin/2.12/maven-surefire-plugin-2.12.jar



mvn install:install-file \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/cms-stub/1.0.0-SNAPSHOT/cms-stub-1.0.0-SNAPSHOT.jar \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/cms-stub/1.0.0-SNAPSHOT/cms-stub-1.0.0-SNAPSHOT.pom

mvn install:install-file \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/emks-stub/1.0.0-SNAPSHOT/emks-stub-1.0.0-SNAPSHOT.jar \
-DgroupId=com.gam.ems-externals -DartifactId=emks-stub -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar

mvn install:install-file \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/5.0.0-SNAPSHOT/portal-stub-5.0.0-SNAPSHOT.jar \
-DgroupId=com.gam.ems-externals -DartifactId=portal-stub -Dversion=5.0.0-SNAPSHOT -Dpackaging=jar



mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/org/apache/maven/surefire/surefire-junit4/2.10/surefire-junit4-2.10.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/org/apache/maven/surefire/surefire-junit4/2.10/surefire-junit4-2.10.jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-Dfile=/home/hamidh/Downloads/maven-surefire-plugin-2.12.4.jar \
-DgroupId=org.apache.maven.plugins -DartifactId=maven-surefire-plugin -Dversion=2.12.4 -Dpackaging=jar

mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/org/apache/maven/surefire/surefire-junit4/2.4.3/surefire-junit4-2.4.3.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/org/apache/maven/surefire/surefire-junit4/2.4.3/surefire-junit4-2.4.3.jar


mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-Dfile=/home/hamidh/Downloads/surefire-junit4-2.12.4.jar \
-DgroupId=org.apache.maven.surefire -DartifactId=surefire-junit4 -Dversion=2.12.4 -Dpackaging=jar


#mvn deploy:deploy-file -DrepositoryId=archiva.snapshots -Durl=http://archiva:8080/repository/snapshots/ \
#-DpomFile=.pom \
#-Dfile=.jar
#


mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/oracle/weblogic/wls-maven-plugin/12.1.1.0/wls-maven-plugin-12.1.1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/com/oracle/weblogic/wls-maven-plugin/12.1.1.0/wls-maven-plugin-12.1.1.0.jar


mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-DpomFile=/home/hamidh/projects/css/m2/repository/com/oracle/weblogic/weblogic/12.1.1.0/weblogic-12.1.1.0.pom \
-Dfile=/home/hamidh/projects/css/m2/repository/com/oracle/weblogic/weblogic/12.1.1.0/weblogic-12.1.1.0.jar



mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/online/1.0/online-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/online/1.0/online-1.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/offline/1.0/offline-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/offline/1.0/offline-1.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/online-sc/1.0/online-sc-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/online-sc/1.0/online-sc-1.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/online-sc/2.0/online-sc-2.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/online-sc/2.0/online-sc-2.0.jar

mvn install:install-file  -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/online-sc/3.0/online-sc-3.0.jar \
-DgroupId=ims-stub -DartifactId=online-sc -Dversion=3.0 -Dpackaging=jar


mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1.0/ims-new-services-1.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1.0/ims-new-services-1.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/2.0.0/ims-new-services-2.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/2.0.0/ims-new-services-2.0.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.3/ims-new-services-1_0.4.3.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.3/ims-new-services-1_0.4.3.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.4/ims-new-services-1_0.4.4.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.4/ims-new-services-1_0.4.4.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.5/ims-new-services-1_0.4.5.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.5/ims-new-services-1_0.4.5.jar

mvn install:install-file  -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.4.6/ims-new-services-1_0.4.6.jar \
-DgroupId=ims-stub -DartifactId=ims-new-services -Dversion=1_0.4.6 -Dpackaging=jar


mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.5.0/ims-new-services-1_0.5.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/ims-stub/ims-new-services/1_0.5.0/ims-new-services-1_0.5.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/1.0.0-SNAPSHOT/ems-externals-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/1.0.0-SNAPSHOT/ems-externals-1.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/1.0.0-SNAPSHOT/ems-externals-1.0.0-20120606.064329-1.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/1.0.0-SNAPSHOT/ems-externals-1.0.0-20120606.064329-1.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/1.0.0-SNAPSHOT/ems-externals-1.0.0-20120606.140521-3.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/1.0.0-SNAPSHOT/ems-externals-1.0.0-20120606.140521-3.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0/matiran-ims-stub-1.0.0.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0/matiran-ims-stub-1.0.0.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/cms-stub/1.0.0-SNAPSHOT/cms-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/cms-stub/1.0.0-SNAPSHOT/cms-stub-1.0.0-SNAPSHOT.jar
mvn install:install-file  -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/pki-stub/1.0.0-SNAPSHOT/pki-stub-1.0.0-SNAPSHOT.jar -DgroupId=com.gam.ems-externals -DartifactId=pki-stub -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar

#mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/emks-stub/1.0.0-SNAPSHOT/emks-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/emks-stub/1.0.0-SNAPSHOT/emks-stub-1.0.0-SNAPSHOT.jar
mvn install:install-file \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/emks-stub/1.0.0-SNAPSHOT/emks-stub-1.0.0-SNAPSHOT.jar \
-DgroupId=com.gam.ems-externals -DartifactId=emks-stub -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar




mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/gaas-stub/1.0.0-SNAPSHOT/gaas-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/gaas-stub/1.0.0-SNAPSHOT/gaas-stub-1.0.0-SNAPSHOT.jar

mvn install:install-file  -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/gaas-stub/2.0.0-SNAPSHOT/gaas-stub-2.0.0-SNAPSHOT.jar \
-DgroupId=com.gam.ems-externals -DartifactId=gaas-stub -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar


mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/1.0.0-SNAPSHOT/portal-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/1.0.0-SNAPSHOT/portal-stub-1.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.0.0-SNAPSHOT/portal-stub-2.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.0.0-SNAPSHOT/portal-stub-2.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.1.0-SNAPSHOT/portal-stub-2.1.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.1.0-SNAPSHOT/portal-stub-2.1.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.1.1-SNAPSHOT/portal-stub-2.1.1-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.1.1-SNAPSHOT/portal-stub-2.1.1-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.2.1-SNAPSHOT/portal-stub-2.2.1-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.2.1-SNAPSHOT/portal-stub-2.2.1-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.2.3-SNAPSHOT/portal-stub-2.2.3-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/2.2.3-SNAPSHOT/portal-stub-2.2.3-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/3.0.0-SNAPSHOT/portal-stub-3.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/3.0.0-SNAPSHOT/portal-stub-3.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/4.0.0-SNAPSHOT/portal-stub-4.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/4.0.0-SNAPSHOT/portal-stub-4.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/5.0.0-SNAPSHOT/portal-stub-5.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/portal-stub/5.0.0-SNAPSHOT/portal-stub-5.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/gam-ims-stub/1.0.0-SNAPSHOT/gam-ims-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/gam-ims-stub/1.0.0-SNAPSHOT/gam-ims-stub-1.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/nocr-sms-stub/1.0.0-SNAPSHOT/nocr-sms-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/nocr-sms-stub/1.0.0-SNAPSHOT/nocr-sms-stub-1.0.0-SNAPSHOT.jar
mvn install:install-file  -DpomFile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0-SNAPSHOT/matiran-ims-stub-1.0.0-SNAPSHOT.pom -Dfile=/home/hamidh/projects/css/m2/repository/com/gam/ems-externals/matiran-ims-stub/1.0.0-SNAPSHOT/matiran-ims-stub-1.0.0-SNAPSHOT.jar




mvn deploy:deploy-file -DrepositoryId=archiva.internal -Durl=http://archiva:8080/repository/internal/ \
-Dfile=/home/hamidh/projects/css/m2/repository/com/gam/nocr/ems/ems-services-ejb-impl/2.1.17/ems-services-ejb-impl-2.1.17.jar \
-DgroupId=com.gam.nocr.ems -DartifactId=ems-services-ejb-impl -Dversion=2.1.17 -Dpackaging=jar


mvn install:install-file  -Dfile=/home/hamidh/projects/css/git/ems/ems-subsystems/pom.xml \
-DgroupId=com.gam.ems-subsystems -DartifactId=pom -Dversion=1.0.0-SNAPSHOT -Dpackaging=pom

