package gruzomarket.reference.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: reference_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReferenceServiceGrpc {

  private ReferenceServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "reference.ReferenceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductRequest,
      gruzomarket.reference.grpc.ProductResponse> getGetProductMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetProduct",
      requestType = gruzomarket.reference.grpc.GetProductRequest.class,
      responseType = gruzomarket.reference.grpc.ProductResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductRequest,
      gruzomarket.reference.grpc.ProductResponse> getGetProductMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductRequest, gruzomarket.reference.grpc.ProductResponse> getGetProductMethod;
    if ((getGetProductMethod = ReferenceServiceGrpc.getGetProductMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetProductMethod = ReferenceServiceGrpc.getGetProductMethod) == null) {
          ReferenceServiceGrpc.getGetProductMethod = getGetProductMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetProductRequest, gruzomarket.reference.grpc.ProductResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetProduct"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetProductRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ProductResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetProduct"))
              .build();
        }
      }
    }
    return getGetProductMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductsByIdsRequest,
      gruzomarket.reference.grpc.ProductListResponse> getGetProductsByIdsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetProductsByIds",
      requestType = gruzomarket.reference.grpc.GetProductsByIdsRequest.class,
      responseType = gruzomarket.reference.grpc.ProductListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductsByIdsRequest,
      gruzomarket.reference.grpc.ProductListResponse> getGetProductsByIdsMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductsByIdsRequest, gruzomarket.reference.grpc.ProductListResponse> getGetProductsByIdsMethod;
    if ((getGetProductsByIdsMethod = ReferenceServiceGrpc.getGetProductsByIdsMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetProductsByIdsMethod = ReferenceServiceGrpc.getGetProductsByIdsMethod) == null) {
          ReferenceServiceGrpc.getGetProductsByIdsMethod = getGetProductsByIdsMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetProductsByIdsRequest, gruzomarket.reference.grpc.ProductListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetProductsByIds"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetProductsByIdsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ProductListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetProductsByIds"))
              .build();
        }
      }
    }
    return getGetProductsByIdsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.SearchProductsRequest,
      gruzomarket.reference.grpc.ProductListResponse> getSearchProductsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchProducts",
      requestType = gruzomarket.reference.grpc.SearchProductsRequest.class,
      responseType = gruzomarket.reference.grpc.ProductListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.SearchProductsRequest,
      gruzomarket.reference.grpc.ProductListResponse> getSearchProductsMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.SearchProductsRequest, gruzomarket.reference.grpc.ProductListResponse> getSearchProductsMethod;
    if ((getSearchProductsMethod = ReferenceServiceGrpc.getSearchProductsMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getSearchProductsMethod = ReferenceServiceGrpc.getSearchProductsMethod) == null) {
          ReferenceServiceGrpc.getSearchProductsMethod = getSearchProductsMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.SearchProductsRequest, gruzomarket.reference.grpc.ProductListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchProducts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.SearchProductsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ProductListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("SearchProducts"))
              .build();
        }
      }
    }
    return getSearchProductsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.DeductStockRequest,
      gruzomarket.reference.grpc.DeductStockResponse> getDeductStockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeductStock",
      requestType = gruzomarket.reference.grpc.DeductStockRequest.class,
      responseType = gruzomarket.reference.grpc.DeductStockResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.DeductStockRequest,
      gruzomarket.reference.grpc.DeductStockResponse> getDeductStockMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.DeductStockRequest, gruzomarket.reference.grpc.DeductStockResponse> getDeductStockMethod;
    if ((getDeductStockMethod = ReferenceServiceGrpc.getDeductStockMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getDeductStockMethod = ReferenceServiceGrpc.getDeductStockMethod) == null) {
          ReferenceServiceGrpc.getDeductStockMethod = getDeductStockMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.DeductStockRequest, gruzomarket.reference.grpc.DeductStockResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeductStock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.DeductStockRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.DeductStockResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("DeductStock"))
              .build();
        }
      }
    }
    return getDeductStockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetCategoryRequest,
      gruzomarket.reference.grpc.CategoryResponse> getGetCategoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCategory",
      requestType = gruzomarket.reference.grpc.GetCategoryRequest.class,
      responseType = gruzomarket.reference.grpc.CategoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetCategoryRequest,
      gruzomarket.reference.grpc.CategoryResponse> getGetCategoryMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetCategoryRequest, gruzomarket.reference.grpc.CategoryResponse> getGetCategoryMethod;
    if ((getGetCategoryMethod = ReferenceServiceGrpc.getGetCategoryMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetCategoryMethod = ReferenceServiceGrpc.getGetCategoryMethod) == null) {
          ReferenceServiceGrpc.getGetCategoryMethod = getGetCategoryMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetCategoryRequest, gruzomarket.reference.grpc.CategoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCategory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetCategoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.CategoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetCategory"))
              .build();
        }
      }
    }
    return getGetCategoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListCategoriesRequest,
      gruzomarket.reference.grpc.CategoryListResponse> getListCategoriesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCategories",
      requestType = gruzomarket.reference.grpc.ListCategoriesRequest.class,
      responseType = gruzomarket.reference.grpc.CategoryListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListCategoriesRequest,
      gruzomarket.reference.grpc.CategoryListResponse> getListCategoriesMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListCategoriesRequest, gruzomarket.reference.grpc.CategoryListResponse> getListCategoriesMethod;
    if ((getListCategoriesMethod = ReferenceServiceGrpc.getListCategoriesMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getListCategoriesMethod = ReferenceServiceGrpc.getListCategoriesMethod) == null) {
          ReferenceServiceGrpc.getListCategoriesMethod = getListCategoriesMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.ListCategoriesRequest, gruzomarket.reference.grpc.CategoryListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListCategories"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ListCategoriesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.CategoryListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ListCategories"))
              .build();
        }
      }
    }
    return getListCategoriesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListCategoryGroupsRequest,
      gruzomarket.reference.grpc.CategoryGroupListResponse> getListCategoryGroupsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCategoryGroups",
      requestType = gruzomarket.reference.grpc.ListCategoryGroupsRequest.class,
      responseType = gruzomarket.reference.grpc.CategoryGroupListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListCategoryGroupsRequest,
      gruzomarket.reference.grpc.CategoryGroupListResponse> getListCategoryGroupsMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListCategoryGroupsRequest, gruzomarket.reference.grpc.CategoryGroupListResponse> getListCategoryGroupsMethod;
    if ((getListCategoryGroupsMethod = ReferenceServiceGrpc.getListCategoryGroupsMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getListCategoryGroupsMethod = ReferenceServiceGrpc.getListCategoryGroupsMethod) == null) {
          ReferenceServiceGrpc.getListCategoryGroupsMethod = getListCategoryGroupsMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.ListCategoryGroupsRequest, gruzomarket.reference.grpc.CategoryGroupListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListCategoryGroups"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ListCategoryGroupsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.CategoryGroupListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ListCategoryGroups"))
              .build();
        }
      }
    }
    return getListCategoryGroupsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetBrandRequest,
      gruzomarket.reference.grpc.BrandResponse> getGetBrandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBrand",
      requestType = gruzomarket.reference.grpc.GetBrandRequest.class,
      responseType = gruzomarket.reference.grpc.BrandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetBrandRequest,
      gruzomarket.reference.grpc.BrandResponse> getGetBrandMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetBrandRequest, gruzomarket.reference.grpc.BrandResponse> getGetBrandMethod;
    if ((getGetBrandMethod = ReferenceServiceGrpc.getGetBrandMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetBrandMethod = ReferenceServiceGrpc.getGetBrandMethod) == null) {
          ReferenceServiceGrpc.getGetBrandMethod = getGetBrandMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetBrandRequest, gruzomarket.reference.grpc.BrandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBrand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetBrandRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.BrandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetBrand"))
              .build();
        }
      }
    }
    return getGetBrandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListBrandsRequest,
      gruzomarket.reference.grpc.BrandListResponse> getListBrandsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListBrands",
      requestType = gruzomarket.reference.grpc.ListBrandsRequest.class,
      responseType = gruzomarket.reference.grpc.BrandListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListBrandsRequest,
      gruzomarket.reference.grpc.BrandListResponse> getListBrandsMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ListBrandsRequest, gruzomarket.reference.grpc.BrandListResponse> getListBrandsMethod;
    if ((getListBrandsMethod = ReferenceServiceGrpc.getListBrandsMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getListBrandsMethod = ReferenceServiceGrpc.getListBrandsMethod) == null) {
          ReferenceServiceGrpc.getListBrandsMethod = getListBrandsMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.ListBrandsRequest, gruzomarket.reference.grpc.BrandListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListBrands"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ListBrandsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.BrandListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ListBrands"))
              .build();
        }
      }
    }
    return getListBrandsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ExistsBrandByNameRequest,
      gruzomarket.reference.grpc.ExistsResponse> getExistsBrandByNameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExistsBrandByName",
      requestType = gruzomarket.reference.grpc.ExistsBrandByNameRequest.class,
      responseType = gruzomarket.reference.grpc.ExistsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ExistsBrandByNameRequest,
      gruzomarket.reference.grpc.ExistsResponse> getExistsBrandByNameMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ExistsBrandByNameRequest, gruzomarket.reference.grpc.ExistsResponse> getExistsBrandByNameMethod;
    if ((getExistsBrandByNameMethod = ReferenceServiceGrpc.getExistsBrandByNameMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getExistsBrandByNameMethod = ReferenceServiceGrpc.getExistsBrandByNameMethod) == null) {
          ReferenceServiceGrpc.getExistsBrandByNameMethod = getExistsBrandByNameMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.ExistsBrandByNameRequest, gruzomarket.reference.grpc.ExistsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExistsBrandByName"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ExistsBrandByNameRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ExistsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ExistsBrandByName"))
              .build();
        }
      }
    }
    return getExistsBrandByNameMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetBrandsByProductRequest,
      gruzomarket.reference.grpc.BrandListResponse> getGetBrandsByProductMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBrandsByProduct",
      requestType = gruzomarket.reference.grpc.GetBrandsByProductRequest.class,
      responseType = gruzomarket.reference.grpc.BrandListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetBrandsByProductRequest,
      gruzomarket.reference.grpc.BrandListResponse> getGetBrandsByProductMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetBrandsByProductRequest, gruzomarket.reference.grpc.BrandListResponse> getGetBrandsByProductMethod;
    if ((getGetBrandsByProductMethod = ReferenceServiceGrpc.getGetBrandsByProductMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetBrandsByProductMethod = ReferenceServiceGrpc.getGetBrandsByProductMethod) == null) {
          ReferenceServiceGrpc.getGetBrandsByProductMethod = getGetBrandsByProductMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetBrandsByProductRequest, gruzomarket.reference.grpc.BrandListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBrandsByProduct"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetBrandsByProductRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.BrandListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetBrandsByProduct"))
              .build();
        }
      }
    }
    return getGetBrandsByProductMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductsByBrandRequest,
      gruzomarket.reference.grpc.ProductListResponse> getGetProductsByBrandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetProductsByBrand",
      requestType = gruzomarket.reference.grpc.GetProductsByBrandRequest.class,
      responseType = gruzomarket.reference.grpc.ProductListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductsByBrandRequest,
      gruzomarket.reference.grpc.ProductListResponse> getGetProductsByBrandMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductsByBrandRequest, gruzomarket.reference.grpc.ProductListResponse> getGetProductsByBrandMethod;
    if ((getGetProductsByBrandMethod = ReferenceServiceGrpc.getGetProductsByBrandMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetProductsByBrandMethod = ReferenceServiceGrpc.getGetProductsByBrandMethod) == null) {
          ReferenceServiceGrpc.getGetProductsByBrandMethod = getGetProductsByBrandMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetProductsByBrandRequest, gruzomarket.reference.grpc.ProductListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetProductsByBrand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetProductsByBrandRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ProductListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetProductsByBrand"))
              .build();
        }
      }
    }
    return getGetProductsByBrandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ExistsProductRequest,
      gruzomarket.reference.grpc.ExistsResponse> getExistsProductMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExistsProduct",
      requestType = gruzomarket.reference.grpc.ExistsProductRequest.class,
      responseType = gruzomarket.reference.grpc.ExistsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ExistsProductRequest,
      gruzomarket.reference.grpc.ExistsResponse> getExistsProductMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.ExistsProductRequest, gruzomarket.reference.grpc.ExistsResponse> getExistsProductMethod;
    if ((getExistsProductMethod = ReferenceServiceGrpc.getExistsProductMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getExistsProductMethod = ReferenceServiceGrpc.getExistsProductMethod) == null) {
          ReferenceServiceGrpc.getExistsProductMethod = getExistsProductMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.ExistsProductRequest, gruzomarket.reference.grpc.ExistsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExistsProduct"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ExistsProductRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ExistsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("ExistsProduct"))
              .build();
        }
      }
    }
    return getExistsProductMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductCategoryIdsRequest,
      gruzomarket.reference.grpc.ProductCategoryIdsResponse> getGetProductCategoryIdsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetProductCategoryIds",
      requestType = gruzomarket.reference.grpc.GetProductCategoryIdsRequest.class,
      responseType = gruzomarket.reference.grpc.ProductCategoryIdsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductCategoryIdsRequest,
      gruzomarket.reference.grpc.ProductCategoryIdsResponse> getGetProductCategoryIdsMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.GetProductCategoryIdsRequest, gruzomarket.reference.grpc.ProductCategoryIdsResponse> getGetProductCategoryIdsMethod;
    if ((getGetProductCategoryIdsMethod = ReferenceServiceGrpc.getGetProductCategoryIdsMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getGetProductCategoryIdsMethod = ReferenceServiceGrpc.getGetProductCategoryIdsMethod) == null) {
          ReferenceServiceGrpc.getGetProductCategoryIdsMethod = getGetProductCategoryIdsMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.GetProductCategoryIdsRequest, gruzomarket.reference.grpc.ProductCategoryIdsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetProductCategoryIds"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.GetProductCategoryIdsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.ProductCategoryIdsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("GetProductCategoryIds"))
              .build();
        }
      }
    }
    return getGetProductCategoryIdsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<gruzomarket.reference.grpc.HealthCheckRequest,
      gruzomarket.reference.grpc.HealthCheckResponse> getHealthCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HealthCheck",
      requestType = gruzomarket.reference.grpc.HealthCheckRequest.class,
      responseType = gruzomarket.reference.grpc.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<gruzomarket.reference.grpc.HealthCheckRequest,
      gruzomarket.reference.grpc.HealthCheckResponse> getHealthCheckMethod() {
    io.grpc.MethodDescriptor<gruzomarket.reference.grpc.HealthCheckRequest, gruzomarket.reference.grpc.HealthCheckResponse> getHealthCheckMethod;
    if ((getHealthCheckMethod = ReferenceServiceGrpc.getHealthCheckMethod) == null) {
      synchronized (ReferenceServiceGrpc.class) {
        if ((getHealthCheckMethod = ReferenceServiceGrpc.getHealthCheckMethod) == null) {
          ReferenceServiceGrpc.getHealthCheckMethod = getHealthCheckMethod =
              io.grpc.MethodDescriptor.<gruzomarket.reference.grpc.HealthCheckRequest, gruzomarket.reference.grpc.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HealthCheck"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  gruzomarket.reference.grpc.HealthCheckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReferenceServiceMethodDescriptorSupplier("HealthCheck"))
              .build();
        }
      }
    }
    return getHealthCheckMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReferenceServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceStub>() {
        @java.lang.Override
        public ReferenceServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReferenceServiceStub(channel, callOptions);
        }
      };
    return ReferenceServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReferenceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceBlockingStub>() {
        @java.lang.Override
        public ReferenceServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReferenceServiceBlockingStub(channel, callOptions);
        }
      };
    return ReferenceServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReferenceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReferenceServiceFutureStub>() {
        @java.lang.Override
        public ReferenceServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReferenceServiceFutureStub(channel, callOptions);
        }
      };
    return ReferenceServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getProduct(gruzomarket.reference.grpc.GetProductRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProductMethod(), responseObserver);
    }

    /**
     */
    default void getProductsByIds(gruzomarket.reference.grpc.GetProductsByIdsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProductsByIdsMethod(), responseObserver);
    }

    /**
     */
    default void searchProducts(gruzomarket.reference.grpc.SearchProductsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchProductsMethod(), responseObserver);
    }

    /**
     */
    default void deductStock(gruzomarket.reference.grpc.DeductStockRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.DeductStockResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeductStockMethod(), responseObserver);
    }

    /**
     */
    default void getCategory(gruzomarket.reference.grpc.GetCategoryRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCategoryMethod(), responseObserver);
    }

    /**
     */
    default void listCategories(gruzomarket.reference.grpc.ListCategoriesRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListCategoriesMethod(), responseObserver);
    }

    /**
     */
    default void listCategoryGroups(gruzomarket.reference.grpc.ListCategoryGroupsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryGroupListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListCategoryGroupsMethod(), responseObserver);
    }

    /**
     */
    default void getBrand(gruzomarket.reference.grpc.GetBrandRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBrandMethod(), responseObserver);
    }

    /**
     */
    default void listBrands(gruzomarket.reference.grpc.ListBrandsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListBrandsMethod(), responseObserver);
    }

    /**
     */
    default void existsBrandByName(gruzomarket.reference.grpc.ExistsBrandByNameRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ExistsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExistsBrandByNameMethod(), responseObserver);
    }

    /**
     */
    default void getBrandsByProduct(gruzomarket.reference.grpc.GetBrandsByProductRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBrandsByProductMethod(), responseObserver);
    }

    /**
     */
    default void getProductsByBrand(gruzomarket.reference.grpc.GetProductsByBrandRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProductsByBrandMethod(), responseObserver);
    }

    /**
     */
    default void existsProduct(gruzomarket.reference.grpc.ExistsProductRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ExistsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExistsProductMethod(), responseObserver);
    }

    /**
     */
    default void getProductCategoryIds(gruzomarket.reference.grpc.GetProductCategoryIdsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductCategoryIdsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProductCategoryIdsMethod(), responseObserver);
    }

    /**
     */
    default void healthCheck(gruzomarket.reference.grpc.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHealthCheckMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ReferenceService.
   */
  public static abstract class ReferenceServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ReferenceServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ReferenceService.
   */
  public static final class ReferenceServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ReferenceServiceStub> {
    private ReferenceServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReferenceServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReferenceServiceStub(channel, callOptions);
    }

    /**
     */
    public void getProduct(gruzomarket.reference.grpc.GetProductRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProductMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getProductsByIds(gruzomarket.reference.grpc.GetProductsByIdsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProductsByIdsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchProducts(gruzomarket.reference.grpc.SearchProductsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchProductsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deductStock(gruzomarket.reference.grpc.DeductStockRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.DeductStockResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeductStockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCategory(gruzomarket.reference.grpc.GetCategoryRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCategoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCategories(gruzomarket.reference.grpc.ListCategoriesRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListCategoriesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCategoryGroups(gruzomarket.reference.grpc.ListCategoryGroupsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryGroupListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListCategoryGroupsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBrand(gruzomarket.reference.grpc.GetBrandRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBrandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listBrands(gruzomarket.reference.grpc.ListBrandsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListBrandsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void existsBrandByName(gruzomarket.reference.grpc.ExistsBrandByNameRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ExistsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExistsBrandByNameMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBrandsByProduct(gruzomarket.reference.grpc.GetBrandsByProductRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBrandsByProductMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getProductsByBrand(gruzomarket.reference.grpc.GetProductsByBrandRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProductsByBrandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void existsProduct(gruzomarket.reference.grpc.ExistsProductRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ExistsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExistsProductMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getProductCategoryIds(gruzomarket.reference.grpc.GetProductCategoryIdsRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductCategoryIdsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProductCategoryIdsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void healthCheck(gruzomarket.reference.grpc.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHealthCheckMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ReferenceService.
   */
  public static final class ReferenceServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ReferenceServiceBlockingStub> {
    private ReferenceServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReferenceServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReferenceServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public gruzomarket.reference.grpc.ProductResponse getProduct(gruzomarket.reference.grpc.GetProductRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProductMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.ProductListResponse getProductsByIds(gruzomarket.reference.grpc.GetProductsByIdsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProductsByIdsMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.ProductListResponse searchProducts(gruzomarket.reference.grpc.SearchProductsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchProductsMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.DeductStockResponse deductStock(gruzomarket.reference.grpc.DeductStockRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeductStockMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.CategoryResponse getCategory(gruzomarket.reference.grpc.GetCategoryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCategoryMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.CategoryListResponse listCategories(gruzomarket.reference.grpc.ListCategoriesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListCategoriesMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.CategoryGroupListResponse listCategoryGroups(gruzomarket.reference.grpc.ListCategoryGroupsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListCategoryGroupsMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.BrandResponse getBrand(gruzomarket.reference.grpc.GetBrandRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBrandMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.BrandListResponse listBrands(gruzomarket.reference.grpc.ListBrandsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListBrandsMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.ExistsResponse existsBrandByName(gruzomarket.reference.grpc.ExistsBrandByNameRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExistsBrandByNameMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.BrandListResponse getBrandsByProduct(gruzomarket.reference.grpc.GetBrandsByProductRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBrandsByProductMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.ProductListResponse getProductsByBrand(gruzomarket.reference.grpc.GetProductsByBrandRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProductsByBrandMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.ExistsResponse existsProduct(gruzomarket.reference.grpc.ExistsProductRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExistsProductMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.ProductCategoryIdsResponse getProductCategoryIds(gruzomarket.reference.grpc.GetProductCategoryIdsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProductCategoryIdsMethod(), getCallOptions(), request);
    }

    /**
     */
    public gruzomarket.reference.grpc.HealthCheckResponse healthCheck(gruzomarket.reference.grpc.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHealthCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ReferenceService.
   */
  public static final class ReferenceServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ReferenceServiceFutureStub> {
    private ReferenceServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReferenceServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReferenceServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ProductResponse> getProduct(
        gruzomarket.reference.grpc.GetProductRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProductMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ProductListResponse> getProductsByIds(
        gruzomarket.reference.grpc.GetProductsByIdsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProductsByIdsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ProductListResponse> searchProducts(
        gruzomarket.reference.grpc.SearchProductsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchProductsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.DeductStockResponse> deductStock(
        gruzomarket.reference.grpc.DeductStockRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeductStockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.CategoryResponse> getCategory(
        gruzomarket.reference.grpc.GetCategoryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCategoryMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.CategoryListResponse> listCategories(
        gruzomarket.reference.grpc.ListCategoriesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListCategoriesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.CategoryGroupListResponse> listCategoryGroups(
        gruzomarket.reference.grpc.ListCategoryGroupsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListCategoryGroupsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.BrandResponse> getBrand(
        gruzomarket.reference.grpc.GetBrandRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBrandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.BrandListResponse> listBrands(
        gruzomarket.reference.grpc.ListBrandsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListBrandsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ExistsResponse> existsBrandByName(
        gruzomarket.reference.grpc.ExistsBrandByNameRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExistsBrandByNameMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.BrandListResponse> getBrandsByProduct(
        gruzomarket.reference.grpc.GetBrandsByProductRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBrandsByProductMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ProductListResponse> getProductsByBrand(
        gruzomarket.reference.grpc.GetProductsByBrandRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProductsByBrandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ExistsResponse> existsProduct(
        gruzomarket.reference.grpc.ExistsProductRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExistsProductMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.ProductCategoryIdsResponse> getProductCategoryIds(
        gruzomarket.reference.grpc.GetProductCategoryIdsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProductCategoryIdsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<gruzomarket.reference.grpc.HealthCheckResponse> healthCheck(
        gruzomarket.reference.grpc.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHealthCheckMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_PRODUCT = 0;
  private static final int METHODID_GET_PRODUCTS_BY_IDS = 1;
  private static final int METHODID_SEARCH_PRODUCTS = 2;
  private static final int METHODID_DEDUCT_STOCK = 3;
  private static final int METHODID_GET_CATEGORY = 4;
  private static final int METHODID_LIST_CATEGORIES = 5;
  private static final int METHODID_LIST_CATEGORY_GROUPS = 6;
  private static final int METHODID_GET_BRAND = 7;
  private static final int METHODID_LIST_BRANDS = 8;
  private static final int METHODID_EXISTS_BRAND_BY_NAME = 9;
  private static final int METHODID_GET_BRANDS_BY_PRODUCT = 10;
  private static final int METHODID_GET_PRODUCTS_BY_BRAND = 11;
  private static final int METHODID_EXISTS_PRODUCT = 12;
  private static final int METHODID_GET_PRODUCT_CATEGORY_IDS = 13;
  private static final int METHODID_HEALTH_CHECK = 14;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_PRODUCT:
          serviceImpl.getProduct((gruzomarket.reference.grpc.GetProductRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductResponse>) responseObserver);
          break;
        case METHODID_GET_PRODUCTS_BY_IDS:
          serviceImpl.getProductsByIds((gruzomarket.reference.grpc.GetProductsByIdsRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse>) responseObserver);
          break;
        case METHODID_SEARCH_PRODUCTS:
          serviceImpl.searchProducts((gruzomarket.reference.grpc.SearchProductsRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse>) responseObserver);
          break;
        case METHODID_DEDUCT_STOCK:
          serviceImpl.deductStock((gruzomarket.reference.grpc.DeductStockRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.DeductStockResponse>) responseObserver);
          break;
        case METHODID_GET_CATEGORY:
          serviceImpl.getCategory((gruzomarket.reference.grpc.GetCategoryRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryResponse>) responseObserver);
          break;
        case METHODID_LIST_CATEGORIES:
          serviceImpl.listCategories((gruzomarket.reference.grpc.ListCategoriesRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryListResponse>) responseObserver);
          break;
        case METHODID_LIST_CATEGORY_GROUPS:
          serviceImpl.listCategoryGroups((gruzomarket.reference.grpc.ListCategoryGroupsRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.CategoryGroupListResponse>) responseObserver);
          break;
        case METHODID_GET_BRAND:
          serviceImpl.getBrand((gruzomarket.reference.grpc.GetBrandRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandResponse>) responseObserver);
          break;
        case METHODID_LIST_BRANDS:
          serviceImpl.listBrands((gruzomarket.reference.grpc.ListBrandsRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandListResponse>) responseObserver);
          break;
        case METHODID_EXISTS_BRAND_BY_NAME:
          serviceImpl.existsBrandByName((gruzomarket.reference.grpc.ExistsBrandByNameRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ExistsResponse>) responseObserver);
          break;
        case METHODID_GET_BRANDS_BY_PRODUCT:
          serviceImpl.getBrandsByProduct((gruzomarket.reference.grpc.GetBrandsByProductRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.BrandListResponse>) responseObserver);
          break;
        case METHODID_GET_PRODUCTS_BY_BRAND:
          serviceImpl.getProductsByBrand((gruzomarket.reference.grpc.GetProductsByBrandRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductListResponse>) responseObserver);
          break;
        case METHODID_EXISTS_PRODUCT:
          serviceImpl.existsProduct((gruzomarket.reference.grpc.ExistsProductRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ExistsResponse>) responseObserver);
          break;
        case METHODID_GET_PRODUCT_CATEGORY_IDS:
          serviceImpl.getProductCategoryIds((gruzomarket.reference.grpc.GetProductCategoryIdsRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.ProductCategoryIdsResponse>) responseObserver);
          break;
        case METHODID_HEALTH_CHECK:
          serviceImpl.healthCheck((gruzomarket.reference.grpc.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<gruzomarket.reference.grpc.HealthCheckResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetProductMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetProductRequest,
              gruzomarket.reference.grpc.ProductResponse>(
                service, METHODID_GET_PRODUCT)))
        .addMethod(
          getGetProductsByIdsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetProductsByIdsRequest,
              gruzomarket.reference.grpc.ProductListResponse>(
                service, METHODID_GET_PRODUCTS_BY_IDS)))
        .addMethod(
          getSearchProductsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.SearchProductsRequest,
              gruzomarket.reference.grpc.ProductListResponse>(
                service, METHODID_SEARCH_PRODUCTS)))
        .addMethod(
          getDeductStockMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.DeductStockRequest,
              gruzomarket.reference.grpc.DeductStockResponse>(
                service, METHODID_DEDUCT_STOCK)))
        .addMethod(
          getGetCategoryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetCategoryRequest,
              gruzomarket.reference.grpc.CategoryResponse>(
                service, METHODID_GET_CATEGORY)))
        .addMethod(
          getListCategoriesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.ListCategoriesRequest,
              gruzomarket.reference.grpc.CategoryListResponse>(
                service, METHODID_LIST_CATEGORIES)))
        .addMethod(
          getListCategoryGroupsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.ListCategoryGroupsRequest,
              gruzomarket.reference.grpc.CategoryGroupListResponse>(
                service, METHODID_LIST_CATEGORY_GROUPS)))
        .addMethod(
          getGetBrandMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetBrandRequest,
              gruzomarket.reference.grpc.BrandResponse>(
                service, METHODID_GET_BRAND)))
        .addMethod(
          getListBrandsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.ListBrandsRequest,
              gruzomarket.reference.grpc.BrandListResponse>(
                service, METHODID_LIST_BRANDS)))
        .addMethod(
          getExistsBrandByNameMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.ExistsBrandByNameRequest,
              gruzomarket.reference.grpc.ExistsResponse>(
                service, METHODID_EXISTS_BRAND_BY_NAME)))
        .addMethod(
          getGetBrandsByProductMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetBrandsByProductRequest,
              gruzomarket.reference.grpc.BrandListResponse>(
                service, METHODID_GET_BRANDS_BY_PRODUCT)))
        .addMethod(
          getGetProductsByBrandMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetProductsByBrandRequest,
              gruzomarket.reference.grpc.ProductListResponse>(
                service, METHODID_GET_PRODUCTS_BY_BRAND)))
        .addMethod(
          getExistsProductMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.ExistsProductRequest,
              gruzomarket.reference.grpc.ExistsResponse>(
                service, METHODID_EXISTS_PRODUCT)))
        .addMethod(
          getGetProductCategoryIdsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.GetProductCategoryIdsRequest,
              gruzomarket.reference.grpc.ProductCategoryIdsResponse>(
                service, METHODID_GET_PRODUCT_CATEGORY_IDS)))
        .addMethod(
          getHealthCheckMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              gruzomarket.reference.grpc.HealthCheckRequest,
              gruzomarket.reference.grpc.HealthCheckResponse>(
                service, METHODID_HEALTH_CHECK)))
        .build();
  }

  private static abstract class ReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReferenceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return gruzomarket.reference.grpc.ReferenceServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReferenceService");
    }
  }

  private static final class ReferenceServiceFileDescriptorSupplier
      extends ReferenceServiceBaseDescriptorSupplier {
    ReferenceServiceFileDescriptorSupplier() {}
  }

  private static final class ReferenceServiceMethodDescriptorSupplier
      extends ReferenceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ReferenceServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ReferenceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReferenceServiceFileDescriptorSupplier())
              .addMethod(getGetProductMethod())
              .addMethod(getGetProductsByIdsMethod())
              .addMethod(getSearchProductsMethod())
              .addMethod(getDeductStockMethod())
              .addMethod(getGetCategoryMethod())
              .addMethod(getListCategoriesMethod())
              .addMethod(getListCategoryGroupsMethod())
              .addMethod(getGetBrandMethod())
              .addMethod(getListBrandsMethod())
              .addMethod(getExistsBrandByNameMethod())
              .addMethod(getGetBrandsByProductMethod())
              .addMethod(getGetProductsByBrandMethod())
              .addMethod(getExistsProductMethod())
              .addMethod(getGetProductCategoryIdsMethod())
              .addMethod(getHealthCheckMethod())
              .build();
        }
      }
    }
    return result;
  }
}
