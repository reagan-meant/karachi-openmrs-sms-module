/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.bahminischeduling;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class BahminischedulingActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public static JdbcTemplate jdbcTemplate;
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Started Bahminischeduling");
		DataSource ds = new SingleConnectionDataSource(Context.getRuntimeProperties().getProperty("connection.url"), Context
		        .getRuntimeProperties().getProperty("connection.username"), Context.getRuntimeProperties().getProperty(
		    "connection.password"), true);
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown Bahminischeduling");
	}
	
}
