#! /usr/bin/env python
#*******************************************************************************
# ALMA - Atacama Large Millimiter Array
# Copyright (c) European Southern Observatory, 2013 
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#
#
# who       when      what
# --------  --------  ----------------------------------------------
# acaproni  2013-06-14  created
#

# Parse a XML log generated by SVN and produces a ChangeLog in twiki format
#
# Assumes that the xml file has been generated with command like
#   svn log -v -xml

import sys
from optparse import OptionParser
from SvnLogToChangeLog import SvnLog

def printHelp(scriptName):
    items=scriptName.split("/")
    scriptName=items[len(items)-1]
    usage="Usage: "+scriptName+""" SVNLOGFILE [-u|--user changesByUser] [-c|--change changeLog]
SVNLOGFILE is the log file generated by svn in XML format
-u|--user fileName       the name of the file with the changes by user
                         The default is changesByUser.twiki
-c||--change fileName    the name of the file with the ChangeLog for the release notes
                         The default is changeLog.twiki
"""
    description = scriptName+""" parses the logs generated by SVN and produces the ChengeLog
for the release notes in twiki format ready to be copied
into the twiki page.

The script generates 2 files whose content is intended to be copied in the 
twiki pages:
changesByUser.twiki: the changes ordered by user name
changeLog.twiki: the changelog for the release notes

The input file, SVNLOGFILE, must be generated with a command like:
svn log -v --xml -r 186519:191288 $SVN_TRUNK_URL/ACS 2>&1 >svnLogACS12.xml
"""
    print
    print usage
    print description
    
def parseArgs():
    
    return (inputFileName,changeLogFileName,changesByUserFileName)
    
if __name__=="__main__":
    # Parse the command line
    changeLogFileName="changeLog.twiki"
    changesByUserFileName="changesByUser.twiki"
    parser = OptionParser()
    parser.add_option("-u", "--user")
    parser.add_option("-c", "--change")
    (options, args) = parser.parse_args()
    if options.change!=None:
        changeLogFileName=options.change
    if options.user!=None:
        changesByUserFileName=options.user
    if len(args)!=1:
        printHelp(sys.argv[0])
        sys.exit(-1)
    else:
        inputFileName=args[0]
    
    svnLog = SvnLog.SvnLog(inputFileName)
    
    print "Generating the changes by user (output in",changesByUserFileName+")"
    # open the file for output
    userOutFile=open(changesByUserFileName,"w")
    svnLog.createChangeLogByUser(userOutFile)
    userOutFile.close()
    print "Changes by user created"
    
    print "Generating the ChangeLog (output in",changeLogFileName+")"
    changeOutFile=open(changeLogFileName,"w")
    svnLog.createChangeLog(changeOutFile)
    changeOutFile.close()
    print "ChangeLog created"
    
    print "Done."
#
# ___oOo___
