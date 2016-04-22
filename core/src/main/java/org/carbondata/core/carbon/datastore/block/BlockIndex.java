/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.carbondata.core.carbon.datastore.block;

import java.util.List;

import org.carbondata.core.carbon.datastore.BTreeBuilderInfo;
import org.carbondata.core.carbon.datastore.BtreeBuilder;
import org.carbondata.core.carbon.datastore.impl.btree.BlockletBtreeBuilder;
import org.carbondata.core.carbon.metadata.leafnode.DataFileMetadata;

/**
 * Class which is responsible for loading the b+ tree block. This class will
 * persist all the detail of a table block
 */
public class BlockIndex extends AbstractIndex {

  /**
   * Below method will be used to load the data block
   *
   * @param blockInfo block detail
   */
  public void buildIndex(List<DataFileMetadata> datFileMetadataList) {
    // create a metadata details
    // this will be useful in query handling
    segmentProperties = new SegmentProperties(datFileMetadataList.get(0).getColumnInTable(),
        datFileMetadataList.get(0).getSegmentInfo().getColumnCardinality());
    // create a segment builder info
    BTreeBuilderInfo indexBuilderInfo =
        new BTreeBuilderInfo(datFileMetadataList, segmentProperties.getDimensionColumnsValueSize());
    BtreeBuilder blocksBuilder = new BlockletBtreeBuilder();
    // load the metadata
    blocksBuilder.build(indexBuilderInfo);
    dataRefNode = blocksBuilder.get();
    totalNumberOfRows = datFileMetadataList.get(0).getNumberOfRows();
  }
}
