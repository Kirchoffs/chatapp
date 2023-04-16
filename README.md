# Notes

## Netty Notes
### Channel & ChannelPipeline
From both the server-side and client-side perspectives, in the entire Netty framework, one connection corresponds to one __Channel__. All processing logic for this Channel is within an object called the __ChannelPipeline__, which is a bidirectional linked list structure. The relationship between the ChannelPipeline and Channel is one-to-one.

Each node in the ChannelPipeline is a __ChannelHandlerContext__ object, which can obtain all contextual information related to the Channel. This object wraps an important object called the __ChannelHandler__, which is responsible for processing logic.

### ChannelHandler
__1\. It has two main interfaces: ChannelInboundHandler & ChannelOutBoundHandler__

ChannelInboundHandler
- main method: __channelRead()__  
- default implementation: __ChannelInboundHandlerAdapter__

ChannelOutboundHandler
- main method: __write()__
- default implementation: __ChannelOutboundHandlerAdapter__

__2\. Call order:__

Code:
```
serverBootstrap
    .childHandler(new ChannelInitializer<NioSocketChannel>() {
        protected void initChannel(NioSocketChannel ch) {
            ch.pipeline().addLast(new InBoundHandlerA());
            ch.pipeline().addLast(new InBoundHandlerB());
            ch.pipeline().addLast(new InBoundHandlerC());
            
            ch.pipeline().addLast(new OutBoundHandlerA());
            ch.pipeline().addLast(new OutBoundHandlerB());
            ch.pipeline().addLast(new OutBoundHandlerC());
        }
    });
```

Graph:
```
inboundA -> inboundB -> inboundC  
outboundA <- outboundB <- outboundC
```

Console output:
```
InboundA
InboundB
InboundC
OutboundC
OutboundB
OutboundA
```

__3\. Inheritance__

- SimpleChannelInboundHandler<T> extends __ChannelInboundHandlerAdapter__ 

- ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements __ChannelInboundHandler__

__4\. Life cycle of ChannelHandler__

```
public class LifeCyCleTestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded()");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered()");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive()");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead()");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive()");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
```

handlerAdded() -> channelRegistered() -> channelActive() -> channelRead() -> channelReadComplete()

channelInactive() -> channelUnregistered() -> handlerRemoved()

__5\. ChannelInitializer__
- initChannel()
- both handlerAdded() and channelRegistered() call initChannel()
- initChannel() uses `initMap.add(ctx)` to avoid re-entrance

### Frame Decoder
- FixedLengthFrameDecoder
- LineBasedFrameDecoder
- DelimiterBasedFrameDecoder
- LengthFieldBasedFrameDecoder

## Other Notes
### Codec
A codec is a device or computer program that encodes or decodes a data stream or signal. Codec is a portmanteau of coder/decoder.

### Java 11 String
- strip(): Removes the white space from both, beginning and the end of string
- stripLeading(): Removes the white space from the beginning
- stripTrailing(): Removes the white space from the end
- isBlank(): Indicates if the String is empty or contains only white space characters