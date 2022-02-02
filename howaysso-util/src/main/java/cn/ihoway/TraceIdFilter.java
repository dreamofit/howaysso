package cn.ihoway;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.apache.log4j.MDC;

import java.util.UUID;

@Activate(group = {"provider", "consumer"})
public class TraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {


        RpcContext rpcContext = RpcContext.getContext();


        String traceId;

        if (rpcContext.isConsumerSide()) {

            traceId = (String) MDC.get("traceId");

            if (traceId == null) {
                traceId = UUID.randomUUID().toString();
            }

            rpcContext.setAttachment("traceId", traceId);

        }

        if (rpcContext.isProviderSide()) {
            traceId = rpcContext.getAttachment("traceId");
            MDC.put("traceId", traceId);
        }

        return invoker.invoke(invocation);
    }
}