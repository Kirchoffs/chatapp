# Notes

## Netty Notes
### Channel & ChannelPipeline
From both the server-side and client-side perspectives, in the entire Netty framework, one connection corresponds to one <b>Channel</b>. All processing logic for this Channel is within an object called the <b>ChannelPipeline</b>, which is a bidirectional linked list structure. The relationship between the ChannelPipeline and Channel is one-to-one.

Each node in the ChannelPipeline is a <b>ChannelHandlerContext</b> object, which can obtain all contextual information related to the Channel. This object wraps an important object called the <b>ChannelHandler</b>, which is responsible for processing logic.

### ChannelHandler
<b>1\. It has two main interfaces: ChannelInboundHandler & ChannelOutBoundHandler</b>

ChannelInboundHandler
- main method: <b>channelRead()</b>  
- default implementation: <b>ChannelInboundHandlerAdapter</b>

ChannelOutboundHandler: write()
- main method: <b>write()</b>
- default implementation: <b>ChannelOutboundHandlerAdapter</b>

<b>2\. Call order:</b>

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

<b>3\. Inheritance</b>

- SimpleChannelInboundHandler<T> extends <b>ChannelInboundHandlerAdapter</b> 

- ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements <b>ChannelInboundHandler</b>