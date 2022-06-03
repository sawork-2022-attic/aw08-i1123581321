# AW08

本次作业承接 aw07 的内容，首先是将运单模块独立出来成为一个单独的 web 服务，该服务仍然通过消息队列接受来自订单模块的消息。每当有消息传递过来时，运单系统会自动生成一个运单。

为了将该服务整合进原有的 micro-pos 系统，使用 spring integration。原本服务的入口是 gateway 服务，spring cloud gateway 基于 webflux，因此定义的 inbound gateway 也是基于 webflux 的 endpoint，基于该 endpoint 定义了一个消息的 channel。channel 的另一端是一个 http outbound gateway，向运单系统发出 http 请求，并且将获取到的请求放回 channel 再由 webflux 的 endpoint 返回给 http 请求的调用方。由此运单系统的服务被整合进了原本的服务中

运行过程仍如 aw07，启动容器中的 rabbitmq 消息队列后依次启动各个 spring-boot 应用，通过 test-pos 项目可以对整体流程进行测试

使用 spring integration 的好处是与外部项目进行整合时增加了一个抽象层。即使整合逻辑简单，该抽象层也能做到解耦的作用，通过 channel 传递 message，使得本应用不依赖于被整合的服务，如果对方更换则只需更换对应的 endpoint 即可。在整合逻辑复杂时，通过 spring integration 提供的 transformer/filter/splitter/aggregator/router/bridge 等机制可以实现更为复杂的处理机制，且不需要对原本的应用做出侵入式的修改，处理逻辑都集中在中间层
