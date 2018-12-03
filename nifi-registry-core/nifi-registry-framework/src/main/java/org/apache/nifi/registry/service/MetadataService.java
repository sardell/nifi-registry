/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.registry.service;

import org.apache.nifi.registry.db.entity.BucketEntity;
import org.apache.nifi.registry.db.entity.BucketItemEntity;
import org.apache.nifi.registry.db.entity.ExtensionBundleEntity;
import org.apache.nifi.registry.db.entity.ExtensionBundleVersionDependencyEntity;
import org.apache.nifi.registry.db.entity.ExtensionBundleVersionEntity;
import org.apache.nifi.registry.db.entity.ExtensionEntity;
import org.apache.nifi.registry.db.entity.ExtensionEntityCategory;
import org.apache.nifi.registry.db.entity.FlowEntity;
import org.apache.nifi.registry.db.entity.FlowSnapshotEntity;
import org.apache.nifi.registry.extension.filter.ExtensionBundleFilterParams;
import org.apache.nifi.registry.extension.filter.ExtensionBundleVersionFilterParams;

import java.util.List;
import java.util.Set;

/**
 * A service for managing metadata about all objects stored by the registry.
 *
 */
public interface MetadataService {

    /**
     * Creates the given bucket.
     *
     * @param bucket the bucket to create
     * @return the created bucket
     */
    BucketEntity createBucket(BucketEntity bucket);

    /**
     * Retrieves the bucket with the given id.
     *
     * @param bucketIdentifier the id of the bucket to retrieve
     * @return the bucket with the given id, or null if it does not exist
     */
    BucketEntity getBucketById(String bucketIdentifier);

    /**
     * Retrieves the buckets with the given name. The name comparison must be case-insensitive.
     *
     * @param name the name of the bucket to retrieve
     * @return the buckets with the given name, or empty list if none exist
     */
    List<BucketEntity> getBucketsByName(String name);

    /**
     * Updates the given bucket, only the name and description should be allowed to be updated.
     *
     * @param bucket the updated bucket to save
     * @return the updated bucket, or null if no bucket with the given id exists
     */
    BucketEntity updateBucket(BucketEntity bucket);

    /**
     * Deletes the bucket, as well as any objects that reference the bucket.
     *
     * @param bucket the bucket to delete
     */
    void deleteBucket(BucketEntity bucket);

    /**
     * Retrieves all buckets with the given ids.
     *
     * @param bucketIds the ids of the buckets to retrieve
     * @return the set of all buckets
     */
    List<BucketEntity> getBuckets(Set<String> bucketIds);

    /**
     * Retrieves all buckets.
     *
     * @return the set of all buckets
     */
    List<BucketEntity> getAllBuckets();

    // --------------------------------------------------------------------------------------------

    /**
     * Retrieves items for the given bucket.
     *
     * @param bucketId the id of bucket to retrieve items for
     * @return the set of items for the bucket
     */
    List<BucketItemEntity> getBucketItems(String bucketId);

    /**
     * Retrieves items for the given buckets.
     *
     * @param bucketIds the ids of buckets to retrieve items for
     * @return the set of items for the bucket
     */
    List<BucketItemEntity> getBucketItems(Set<String> bucketIds);

    // --------------------------------------------------------------------------------------------

    /**
     * Creates a versioned flow in the given bucket.
     *
     * @param flow the versioned flow to create
     * @return the created versioned flow
     * @throws IllegalStateException if no bucket with the given identifier exists
     */
    FlowEntity createFlow(FlowEntity flow);

    /**
     * Retrieves the versioned flow with the given id and DOES NOT populate the versionCount.
     *
     * @param flowIdentifier the identifier of the flow to retrieve
     * @return the versioned flow with the given id, or null if no flow with the given id exists
     */
    FlowEntity getFlowById(String flowIdentifier);

    /**
     * Retrieves the versioned flow with the given id and DOES populate the versionCount.
     *
     * @param flowIdentifier the identifier of the flow to retrieve
     * @return the versioned flow with the given id, or null if no flow with the given id exists
     */
    FlowEntity getFlowByIdWithSnapshotCounts(String flowIdentifier);

    /**
     * Retrieves the versioned flows with the given name. The name comparison must be case-insensitive.
     *
     * @param name the name of the flow to retrieve
     * @return the versioned flows with the given name, or empty list if no flows with the given name exists
     */
    List<FlowEntity> getFlowsByName(String name);

    /**
     * Retrieves the versioned flows with the given name in the given bucket. The name comparison must be case-insensitive.
     *
     * @param  bucketIdentifier the identifier of the bucket
     * @param name the name of the flow to retrieve
     * @return the versioned flows with the given name in the given bucket, or empty list if no flows with the given name exists
     */
    List<FlowEntity> getFlowsByName(String bucketIdentifier, String name);

    /**
     * Retrieves the versioned flows for the given bucket.
     *
     * @param bucketIdentifier the bucket id to retrieve flows for
     * @return the flows in the given bucket
     */
    List<FlowEntity> getFlowsByBucket(String bucketIdentifier);

    /**
     * Updates the given versioned flow, only the name and description should be allowed to be updated.
     *
     * @param flow the updated versioned flow to save
     * @return the updated versioned flow
     */
    FlowEntity updateFlow(FlowEntity flow);

    /**
     * Deletes the flow if one exists.
     *
     * @param flow the flow to delete
     */
    void deleteFlow(FlowEntity flow);

    // --------------------------------------------------------------------------------------------

    /**
     * Creates a versioned flow snapshot.
     *
     * @param flowSnapshot the snapshot to create
     * @return the created snapshot
     * @throws IllegalStateException if the versioned flow specified by flowSnapshot.getFlowIdentifier() does not exist
     */
    FlowSnapshotEntity createFlowSnapshot(FlowSnapshotEntity flowSnapshot);

    /**
     * Retrieves the snapshot for the given flow identifier and snapshot version.
     *
     * @param flowIdentifier the identifier of the flow the snapshot belongs to
     * @param version the version of the snapshot
     * @return the versioned flow snapshot for the given flow identifier and version, or null if none exists
     */
    FlowSnapshotEntity getFlowSnapshot(String flowIdentifier, Integer version);

    /**
     * Retrieves the snapshot with the latest version number for the given flow in the given bucket.
     *
     * @param flowIdentifier the id of flow to retrieve the latest snapshot for
     * @return the latest snapshot for the flow, or null if one doesn't exist
     */
    FlowSnapshotEntity getLatestSnapshot(String flowIdentifier);

    /**
     * Retrieves the snapshots for the given flow in the given bucket.
     *
     * @param flowIdentifier the id of the flow
     * @return the snapshots
     */
    List<FlowSnapshotEntity> getSnapshots(String flowIdentifier);

    /**
     * Deletes the flow snapshot.
     *
     * @param flowSnapshot the flow snapshot to delete
     */
    void deleteFlowSnapshot(FlowSnapshotEntity flowSnapshot);

    // --------------------------------------------------------------------------------------------

    /**
     * Creates the given extension bundle.
     *
     * @param extensionBundle the extension bundle to create
     * @return the created extension bundle
     */
    ExtensionBundleEntity createExtensionBundle(ExtensionBundleEntity extensionBundle);

    /**
     * Retrieves the extension bundle with the given id.
     *
     * @param extensionBundleId the id of the extension bundle
     * @return the extension bundle with the id, or null if one does not exist
     */
    ExtensionBundleEntity getExtensionBundle(String extensionBundleId);

    /**
     * Retrieves the extension bundle in the given bucket with the given group and artifact id.
     *
     * @return the extension bundle, or null if one does not exist
     */
    ExtensionBundleEntity getExtensionBundle(String bucketId, String groupId, String artifactId);

    /**
     * Retrieves all extension bundles in the buckets with the given bucket ids.
     *
     * @param bucketIds the bucket ids
     * @param filterParams the optional filter params
     * @return the list of all extension bundles in the given buckets
     */
    List<ExtensionBundleEntity> getExtensionBundles(Set<String> bucketIds, ExtensionBundleFilterParams filterParams);

    /**
     * Retrieves the extension bundles for the given bucket.
     *
     * @param bucketId the bucket id
     * @return the list of extension bundles for the bucket
     */
    List<ExtensionBundleEntity> getExtensionBundlesByBucket(String bucketId);

    /**
     * Retrieves the extension bundles for the given bucket and group.
     *
     * @param bucketId the bucket id
     * @param groupId the group id
     * @return the list of extension bundles for the bucket and group
     */
    List<ExtensionBundleEntity> getExtensionBundlesByBucketAndGroup(String bucketId, String groupId);

    /**
     * Deletes the given extension bundle.
     *
     * @param extensionBundle the extension bundle to delete
     */
    void deleteExtensionBundle(ExtensionBundleEntity extensionBundle);

    /**
     * Deletes the extension bundle with the given id.
     *
     * @param extensionBundleId the id extension bundle to delete
     */
    void deleteExtensionBundle(String extensionBundleId);

    // --------------------------------------------------------------------------------------------

    /**
     * Creates a version of an extension bundle.
     *
     * @param extensionBundleVersion the bundle version to create
     * @return the created bundle version
     */
    ExtensionBundleVersionEntity createExtensionBundleVersion(ExtensionBundleVersionEntity extensionBundleVersion);

    /**
     * Retrieves the extension bundle version for the given bundle id and version.
     *
     * @param extensionBundleId the id of the extension bundle
     * @param version the version of the extension bundle
     * @return the extension bundle version, or null if does not exist
     */
    ExtensionBundleVersionEntity getExtensionBundleVersion(String extensionBundleId, String version);

    /**
     * Retrieves the extension bundle version by bucket, group, artifact, version.
     *
     * @param bucketId the bucket id
     * @param groupId the group id
     * @param artifactId the artifact id
     * @param version the version
     * @return the extension bundle version, or null if does not exist
     */
    ExtensionBundleVersionEntity getExtensionBundleVersion(String bucketId, String groupId, String artifactId, String version);

    /**
     * Retrieves the extension bundle versions in the given buckets, matching the optional filter parameters.
     *
     * @param bucketIdentifiers the bucket identifiers
     * @param filterParams the optional filter params
     * @return the extension bundle versions
     */
    List<ExtensionBundleVersionEntity> getExtensionBundleVersions(Set<String> bucketIdentifiers, ExtensionBundleVersionFilterParams filterParams);

    /**
     * Retrieves the extension bundle versions for the given extension bundle id.
     *
     * @param extensionBundleId the extension bundle id
     * @return the list of extension bundle versions
     */
    List<ExtensionBundleVersionEntity> getExtensionBundleVersions(String extensionBundleId);

    /**
     * Retrieves the extension bundle version with the given group id and artifact id in the given bucket.
     *
     * @param bucketId the bucket id
     * @param groupId the group id
     * @param artifactId the artifact id
     * @return the list of extension bundles
     */
    List<ExtensionBundleVersionEntity> getExtensionBundleVersions(String bucketId, String groupId, String artifactId);

    /**
     * Retrieves the extension bundle versions with the given group id, artifact id, and version across all buckets.
     *
     * @param groupId the group id
     * @param artifactId the artifact id
     * @param version the versions
     * @return all bundle versions for the group id, artifact id, and version
     */
    List<ExtensionBundleVersionEntity> getExtensionBundleVersionsGlobal(String groupId, String artifactId, String version);

    /**
     * Deletes the extension bundle version.
     *
     * @param extensionBundleVersion the extension bundle version to delete
     */
    void deleteExtensionBundleVersion(ExtensionBundleVersionEntity extensionBundleVersion);

    /**
     * Deletes the extension bundle version.
     *
     * @param extensionBundleVersionId the id of the extension bundle version
     */
    void deleteExtensionBundleVersion(String extensionBundleVersionId);

    // --------------------------------------------------------------------------------------------

    /**
     * Creates the given extension bundle version dependency.
     *
     * @param dependencyEntity the dependency entity
     * @return the created dependency
     */
    ExtensionBundleVersionDependencyEntity createDependency(ExtensionBundleVersionDependencyEntity dependencyEntity);

    /**
     * Retrieves the bundle dependencies for the given bundle version.
     *
     * @param extensionBundleVersionId the id of the extension bundle version
     * @return the list of dependencies
     */
    List<ExtensionBundleVersionDependencyEntity> getDependenciesForBundleVersion(String extensionBundleVersionId);

    // --------------------------------------------------------------------------------------------

    /**
     * Creates the given extension.
     *
     * @param extension the extension to create
     * @return the created extension
     */
    ExtensionEntity createExtension(ExtensionEntity extension);

    /**
     * Retrieves the extension with the given id.
     *
     * @param id the id of the extension
     * @return the extension with the id, or null if one does not exist
     */
    ExtensionEntity getExtensionById(String id);

    /**
     * Retrieves all extensions.
     *
     * @return the list of all extensions
     */
    List<ExtensionEntity> getAllExtensions();

    /**
     * Retrieves the extensions for the given extension bundle version.
     *
     * @param extensionBundleVersionId the id of the extension bundle version
     * @return the extensions in the given bundle
     */
    List<ExtensionEntity> getExtensionsByBundleVersionId(String extensionBundleVersionId);

    /**
     * Retrieves the extensions for the bundle in the given bucket with the given group, artifact, and version.
     *
     * @param bucketId the bucket of the bundle
     * @param groupId the group of the bundle
     * @param artifactId the artifact id of the bundle
     * @param version the version of the bundle
     * @return the extensions for the bundle
     */
    List<ExtensionEntity> getExtensionsByBundleCoordinate(String bucketId, String groupId, String artifactId, String version);

    /**
     * Retrieves the extensions for the given category (i.e. processor, controller service, reporting task).
     *
     * @param category the category
     * @return the extensions for the given category
     */
    List<ExtensionEntity> getExtensionsByCategory(ExtensionEntityCategory category);

    /**
     * Retrieves the extensions with the given tag.
     *
     * @param tag the tag
     * @return the extensions with the given tag
     */
    List<ExtensionEntity> getExtensionsByTag(String tag);

    /**
     * Retrieves the set of all extension tags.
     *
     * @return the set of all extension tags
     */
    Set<String> getAllExtensionTags();

    /**
     * Deletes the given extension.
     *
     * @param extension the extension to delete
     */
    void deleteExtension(ExtensionEntity extension);

    // --------------------------------------------------------------------------------------------

    /**
     * @return the set of field names for Buckets
     */
    Set<String> getBucketFields();

    /**
     * @return the set of field names for BucketItems
     */
    Set<String> getBucketItemFields();

    /**
     * @return the set of field names for Flows
     */
    Set<String> getFlowFields();

}