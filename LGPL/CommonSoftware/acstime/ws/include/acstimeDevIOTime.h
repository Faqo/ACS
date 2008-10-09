/* @(#) $Id: acstimeDevIOTime.h,v 1.13 2008/10/09 07:31:16 cparedes Exp $
 *
 * Copyright (C) 2001
 * Associated Universities, Inc. Washington DC, USA.
 *
 * Produced for the ALMA project
 *
 * This library is free software; you can redistribute it and/or modify it it 
 * under the terms of the GNU Library General Public License as published by 
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Library General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 675 Massachusetts Ave, Cambridge, MA, 02139, USA.
 *
 * Correspondence concerning ALMA should be addressed as follows:
 * Internet email: alma-sw-admin@nrao.edu
 */
////////////////////////////////////////////////////////////////////////
#ifndef ACSTIME_DEVIO_TIME_H
#define ACSTIME_DEVIO_TIME_H
////////////////////////////////////////////////////////////////////////
#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif
////////////////////////////////////////////////////////////////////////
#include <baciDevIO.h>
#include "acstimeTimeUtil.h"
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
/** @file acstimeDevIOTime.h
 *  Header file for Clock's time devIO. 
 */

/**
 *  @class DevIOTime
 *  This class is derived from devIO and is intended to be used as a clock.  In other 
 *  words, every time get_sync() of an uLongLong BACI property is 
 *  invoked that utilizes this devIO, the current ACS time in 100ns format will be 
 *  returned.
 *
 *  TODO: 
 *  - fill out the errcode of write() because it should never be invoked.
 *    That is, this devIO should <b>never be used with RWuLongLong properties!</b>
 */
class DevIOTime : public DevIO<ACS::Time>
{
  public:
    /**
     * Constructor - nothing to create!
     */
    DevIOTime() {}
    
    /**
     * Destructor - nothing to destroy!
     */
    virtual ~DevIOTime() {}
    
    /**
     *  DWF-why is this needed???
     * @htmlonly
       <br><hr>
       @endhtmlonly
     */
    virtual bool 
    initializeValue() {return true;}
    
    /**
     * Retrieves the current time using ACE methods and then converts it 
     * to "ACS time".
     * @param timestamp Time for an ACS Completion.
     * @return The ACS-ized time we just retrieved.
     * @throw ACSErr::ACSbaseExImpl
     * @htmlonly
       <br><hr>
       @endhtmlonly
     */     
    virtual ACS::Time 
    read(ACS::Time &timestamp)
	{
	    timestamp = getTimeStamp();
	    
	    acstime::Epoch retValue = TimeUtil::ace2epoch(ACE_OS::gettimeofday());
	    return (ACS::Time)retValue.value;
	}
    
    /**
     *  DWF-providing an implementation of this method does not make sense, but
     *  it would be abstract otherwise...
     * @throw ACSErr::ACSbaseExImpl
     * @htmlonly
       <br><hr>
       @endhtmlonly
     */
    virtual void 
    write(const ACS::Time &value, ACS::Time &timestamp)
	{
	    ACE_UNUSED_ARG(value);
	    timestamp = getTimeStamp();
	}
};
#endif

