// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: adf.proto

package com.proto.transporter;

public interface ObjectListOrBuilder extends
    // @@protoc_insertion_point(interface_extends:ObjectList)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .Object objects = 1;</code>
   */
  java.util.List<com.proto.transporter.Object> 
      getObjectsList();
  /**
   * <code>repeated .Object objects = 1;</code>
   */
  com.proto.transporter.Object getObjects(int index);
  /**
   * <code>repeated .Object objects = 1;</code>
   */
  int getObjectsCount();
  /**
   * <code>repeated .Object objects = 1;</code>
   */
  java.util.List<? extends com.proto.transporter.ObjectOrBuilder> 
      getObjectsOrBuilderList();
  /**
   * <code>repeated .Object objects = 1;</code>
   */
  com.proto.transporter.ObjectOrBuilder getObjectsOrBuilder(
      int index);

  /**
   * <code>string uuid = 2;</code>
   * @return The uuid.
   */
  java.lang.String getUuid();
  /**
   * <code>string uuid = 2;</code>
   * @return The bytes for uuid.
   */
  com.google.protobuf.ByteString
      getUuidBytes();
}
