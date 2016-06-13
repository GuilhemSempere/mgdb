/*******************************************************************************
 * MGDB - Mongo Genotype DataBase
 * Copyright (C) 2016 <CIRAD>
 *     
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * See <http://www.gnu.org/licenses/gpl-3.0.html> for details about
 * GNU Affero General Public License V3.
 *******************************************************************************/
package fr.cirad.mgdb.model.mongo.maintypes;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * The Class AutoIncrementCounter.
 */
@Document(collection="counters")
@TypeAlias("AIC")
public class AutoIncrementCounter
{

	/** The id. */
	@Id private String id;

	/** The seq. */
	private int seq;

//	public Counter(String id, int seq) {
//		super();
//		this.id = id;
//		this.seq = seq;
//	}

	/**
 * Instantiates a new auto increment counter.
 *
 * @param id the id
 * @param seq the seq
 */
public AutoIncrementCounter(String id, int seq) {
		super();
		this.id = id;
		this.seq = seq;
	}

	/**
	 * Gets the seq.
	 *
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * Sets the seq.
	 *
	 * @param seq the new seq
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * Gets the next sequence.
	 *
	 * @param mongo the mongo
	 * @param collectionName the collection name
	 * @return the next sequence
	 */
	static synchronized public int getNextSequence(MongoOperations mongo, String collectionName)
	{
		AutoIncrementCounter counter = mongo.findAndModify(new Query(Criteria.where("_id").is(collectionName)), new Update().inc("seq", 1), FindAndModifyOptions.options().returnNew(true), AutoIncrementCounter.class);
		if (counter != null)
			return counter.getSeq();

		// counters collection contains no data for this type
		counter = new AutoIncrementCounter(collectionName, 1);
//		counter.setSeq(1);
		mongo.save(counter);
		return 1;
	}
}