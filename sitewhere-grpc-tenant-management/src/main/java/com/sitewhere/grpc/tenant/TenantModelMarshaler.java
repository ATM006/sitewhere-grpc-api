/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.grpc.tenant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sitewhere.grpc.common.CommonModelConverter;
import com.sitewhere.grpc.model.TenantModel.GTenantModelUpdate;
import com.sitewhere.grpc.model.TenantModel.GTenantModelUpdateType;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.tenant.ITenant;

public class TenantModelMarshaler {

    /**
     * Build message that reflects a tenant model update.
     * 
     * @param type
     * @param tenant
     * @return
     * @throws SiteWhereException
     */
    public static byte[] buildTenantModelUpdateMessage(GTenantModelUpdateType type, ITenant tenant)
	    throws SiteWhereException {
	GTenantModelUpdate.Builder update = GTenantModelUpdate.newBuilder();
	update.setType(type);
	update.setTenant(TenantModelConverter.asGrpcTenant(tenant));
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	try {
	    update.build().writeTo(output);
	    return output.toByteArray();
	} catch (IOException e) {
	    throw new SiteWhereException("Unable to build tenant update message.", e);
	} finally {
	    CommonModelConverter.closeQuietly(output);
	}
    }

    /**
     * Parse message that reflects a tenant model update.
     * 
     * @param payload
     * @return
     * @throws SiteWhereException
     */
    public static GTenantModelUpdate parseTenantModelUpdateMessage(byte[] payload) throws SiteWhereException {
	try {
	    return GTenantModelUpdate.parseFrom(payload);
	} catch (InvalidProtocolBufferException e) {
	    throw new SiteWhereException("Unable to parse tenant update message.", e);
	}
    }
}
