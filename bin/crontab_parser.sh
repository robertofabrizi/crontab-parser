#!/bin/bash

dir=`dirname $0`
FILE_PATH=`cd $dir;pwd`
MON_HOME=${FILE_PATH%/*}
LIB_PATH=${MON_HOME}/lib
BIN_PATH=${MON_HOME}/bin
CONFIG_PATH=${MON_HOME}/config
LOG_PATH=${MON_HOME}/log
DATA_LOG=`date +%d-%m-%y_%T`

if [ ${JAVA_HOME} ] ; then
	echo "The variable JAVA_HOME is defined"
else
	echo "The variable JAVA_HOME is not defined as an environment variable" > ${LOG_PATH}/err_$DATA_LOG.log
	if [ $1 ] ; then
       		echo "The variable JAVA_HOME is passed via the command line"
		JAVA_HOME=${1}
	else
        	echo "The variable JAVA_HOME is not passed via the command line" > ${LOG_PATH}/err_$DATA_LOG.log
	exit
	fi
fi

JAVA_BIN=${JAVA_HOME}/bin

classpath()
{
        CLASSPATH=${CONFIG_PATH}
        for i in `ls $LIB_PATH/*.jar`; do
                CLASSPATH=$CLASSPATH:$i
        done
        export CLASSPATH
}

JVM_ARGS="-XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -Xmx512M -Dlog4j.configuration=file:${CONFIG_PATH}/log4j.properties -Dlog_path=${LOG_PATH}"

classpath

${JAVA_BIN}/java -cp ${CLASSPATH} ${JVM_ARGS} com.rhad.crontabparser.Main >> ${LOG_PATH}/out_$DATA_LOG.log 2>&1
