/*
 *
 * Copyright (c) 2017 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.radanalytics.als

import java.util
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import collection.JavaConverters._

object ALSSerializer {

  private def unpack(factor: util.ArrayList[Any]): (Int, Array[Double]) = {
    (factor.get(0).asInstanceOf[Int],
     factor.get(1).asInstanceOf[util.ArrayList[Double]].asScala.toArray)
  }

  def instantiateModel(sc: JavaSparkContext,
                       rank: Int,
                       userFactors: util.ArrayList[util.ArrayList[Any]],
                       productFactors: util.ArrayList[util.ArrayList[Any]])
    : MatrixFactorizationModel = {

    val userRDD = sc.parallelize(userFactors.asScala.map(unpack))
    val productRDD = sc.parallelize(productFactors.asScala.toList.map(unpack))

    new MatrixFactorizationModel(rank = rank,
                                 userFeatures = userRDD,
                                 productFeatures = productRDD)

  }

}