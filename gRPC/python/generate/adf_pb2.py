# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: adf.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\tadf.proto\"?\n\x06Object\x12\n\n\x02id\x18\x01 \x01(\x03\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\x1b\n\x08priority\x18\x03 \x01(\x0b\x32\t.Priority\"\x19\n\x08Priority\x12\r\n\x05value\x18\x01 \x01(\x01\"&\n\nObjectList\x12\x18\n\x07objects\x18\x01 \x03(\x0b\x32\x07.Object2<\n\nCallPython\x12.\n\x10GetSortedObjects\x12\x0b.ObjectList\x1a\x0b.ObjectList\"\x00\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'adf_pb2', _globals)
if _descriptor._USE_C_DESCRIPTORS == False:
  DESCRIPTOR._options = None
  _globals['_OBJECT']._serialized_start=13
  _globals['_OBJECT']._serialized_end=76
  _globals['_PRIORITY']._serialized_start=78
  _globals['_PRIORITY']._serialized_end=103
  _globals['_OBJECTLIST']._serialized_start=105
  _globals['_OBJECTLIST']._serialized_end=143
  _globals['_CALLPYTHON']._serialized_start=145
  _globals['_CALLPYTHON']._serialized_end=205
# @@protoc_insertion_point(module_scope)
