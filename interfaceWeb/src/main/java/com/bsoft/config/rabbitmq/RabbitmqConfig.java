//package com.bsoft.config.rabbitmq;
//
//import com.bsoft.constants.InterfaceConstants;
//import com.bsoft.entity.BillListBean;
//import com.bsoft.model.HospitalInfo;
//import com.bsoft.model.RequestRecord;
//import com.bsoft.service.BillListService;
//import com.bsoft.service.HospitalInfoService;
//import com.bsoft.service.RequestRecordService;
//import com.bsoft.util.CommonUtil;
//import com.rabbitmq.client.Channel;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Configuration
//public class RabbitmqConfig {
//    @Autowired
//    StringRedisTemplate redisTemplate;
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Value("${spring.rabbitmq.port}")
//    private int port;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//    @Value("${billMethod}")
//    private String billMethod;
//
//    @Resource
//    private RequestRecordService requestRecordService;
//
//    @Resource
//    private BillListService billListService;
//
//    @Resource
//    private HospitalInfoService hospitalInfoService;
//
//
//    public static final String EXCHANGE_A = "my-mq-exchange_A";
//    public static final String EXCHANGE_B = "my-mq-exchange_B";
//    public static final String EXCHANGE_C = "my-mq-exchange_C";
//
//
//    public static final String QUEUE_A = "QUEUE_A";
//    public static final String QUEUE_B = "QUEUE_B";
//    public static final String QUEUE_C = "QUEUE_C";
//    public static final String QUEUE_D = "eurekaInfo";
//
//    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
//    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
//    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost("/");
//        connectionFactory.setPublisherConfirms(true);
//        return connectionFactory;
//    }
//
//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    //必须是prototype类型
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        return template;
//    }
//
//
//    /**
//     * 针对消费者配置
//     * 1. 设置交换机类型
//     * 2. 将队列绑定到交换机
//     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
//     * HeadersExchange ：通过添加属性key-value匹配
//     * DirectExchange:按照routingkey分发到指定队列
//     * TopicExchange:多关键字匹配
//     */
//    @Bean
//    public DirectExchange defaultExchange() {
//        return new DirectExchange(EXCHANGE_A);
//    }
//
//
//    /**
//     * 获取队列A
//     *
//     * @return
//     */
//    @Bean
//    public Queue queueA() {
//        return new Queue(QUEUE_A, true); //队列持久
//    }
//
//    @Bean
//    public Queue queueB() {
//
//        return new Queue(QUEUE_B, true); //队列持久
//    }
//
//    @Bean
//    public Queue queueD() {
//        return new Queue(QUEUE_D, true); //队列持久
//    }
//    //一个交换机可以绑定多个消息队列，也就是消息通过一个交换机，可以分发到不同的队列当中去。
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(ROUTINGKEY_A);
//    }
//
//    @Bean
//    public Binding bindingB() {
//        return BindingBuilder.bind(queueB()).to(defaultExchange()).with(ROUTINGKEY_B);
//    }
//
//
//    @Bean
//    public SimpleMessageListenerContainer messageContainerA() {
//        //加载处理消息A的队列
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        //设置接收多个队列里面的消息，这里设置接收队列A
//        //假如想一个消费者处理多个队列里面的信息可以如下设置：
//        //container.setQueues(queueA(),queueB(),queueC());
//        container.setQueues(queueA());
//        container.setExposeListenerChannel(true);
//        //设置最大的并发的消费者数量
//        container.setMaxConcurrentConsumers(10);
//        //最小的并发消费者的数量
//        container.setConcurrentConsumers(1);
//        //设置确认模式手工确认
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                /**通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，
//                 换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它 */
//                channel.basicQos(10);
//                byte[] body = message.getBody();
//                try {
//                    logger.info("接收处理队列A当中的消息:" + new String(body, "UTF-8"));
//
//                    String request = new String(body, "UTF-8");
//                    JSONObject requestTemp = JSONObject.fromObject(request);
//                    HospitalInfo hospitalInfo = (HospitalInfo) JSONObject.toBean(requestTemp.getJSONObject("hospitalInfo"), HospitalInfo.class);
//
//                    requestTemp.remove("hospitalInfo");
//                    RequestRecord tempRecord = (RequestRecord) JSONObject.toBean(requestTemp, RequestRecord.class);
//
//
//                    HospitalInfo temp = hospitalInfoService.get(hospitalInfo.getId());
//                    if (temp != null) {
//                        hospitalInfo.setRequestCount(temp.getRequestCount() + 1);
//                        hospitalInfo.setErrorCount(temp.getErrorCount() + 1);
//                        hospitalInfo.setSuccessCount(temp.getSuccessCount());
//                        hospitalInfoService.updateById(hospitalInfo.getId(), hospitalInfo);
//                    } else {
//                        hospitalInfo.setRequestCount(1l);
//                        hospitalInfo.setErrorCount(1l);
//                        hospitalInfo.setSuccessCount(0l);
//                        hospitalInfoService.save(hospitalInfo);
//                    }
//
//                    requestRecordService.save(tempRecord);
//
//
//                    /**为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
//                     当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    /**为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
//                     当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
//                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                }
//
//            }
//        });
//        return container;
//    }
//
//
//    @Bean
//    public SimpleMessageListenerContainer messageContainerB() {
//        //加载处理消息A的队列
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        //设置接收多个队列里面的消息，这里设置接收队列A
//        //假如想一个消费者处理多个队列里面的信息可以如下设置：
//        //container.setQueues(queueA(),queueB(),queueC());
//        container.setQueues(queueB());
//        container.setExposeListenerChannel(true);
//        //设置最大的并发的消费者数量
//        container.setMaxConcurrentConsumers(10);
//        //最小的并发消费者的数量
//        container.setConcurrentConsumers(1);
//        //设置确认模式手工确认
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                /**通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，
//                 换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它 */
//                channel.basicQos(1);
//                byte[] body = message.getBody();
//                try {
//                    logger.info("接收处理队列B当中的消息:" + new String(body, "UTF-8"));
//
//                    String request = new String(body, "UTF-8");
//                    JSONObject requestTemp = JSONObject.fromObject(request);
//
//                    HospitalInfo hospitalInfo = (HospitalInfo) JSONObject.toBean(requestTemp.getJSONObject("hospitalInfo"), HospitalInfo.class);
//
//                    requestTemp.remove("hospitalInfo");
//
//                    RequestRecord tempRecord = (RequestRecord) JSONObject.toBean(requestTemp, RequestRecord.class);
//
//                    HospitalInfo temp = hospitalInfoService.get(hospitalInfo.getId());
//                    if (temp != null) {
//                        hospitalInfo.setRequestCount(temp.getRequestCount());
//                        hospitalInfo.setSuccessCount(temp.getSuccessCount() + 1);
//                        hospitalInfo.setErrorCount(temp.getErrorCount() - 1);
//                        hospitalInfoService.updateById(hospitalInfo.getId(), hospitalInfo);
//                    } else {
//                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                    }
//                    //如果是收费接口，则保存一个账单信息
//                    if (billMethod.contains(tempRecord.getMethodId()) && (InterfaceConstants.SYSTEMATIC_SUCCESS_CODE + "").equals(tempRecord.getStatus())) {
//                        svaeBillListinfo(tempRecord);
//                    }
//                    requestRecordService.updateById(tempRecord.getId(), tempRecord);
//
//
//                    /**为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
//                     当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    /**为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
//                     当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
//                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                }
//
//            }
//        });
//        return container;
//    }
//
//
//    /***
//     * 保存收费接口信息
//     * @param tempRecord
//     */
//    public void svaeBillListinfo(RequestRecord tempRecord) {
//        BillListBean billListBean = new BillListBean();
//        BeanUtils.copyProperties(tempRecord, billListBean);
//        JSONObject jsonObj = JSONObject.fromObject(tempRecord.getRequestParams());
////        支付金额
//        billListBean.setPaymentAmount(CommonUtil.changeY2F(Double.parseDouble(jsonObj.get("payAmount") == null ? jsonObj.get("paymentAmount") + "" : jsonObj.get("payAmount") + "")));
////        支付订单号
//        billListBean.setWcoerderId(jsonObj.get("wcoerderId") + "");
//        String payType = "ZFFS_QT";
//        if (jsonObj.get("source").toString().contains("_WX_")) {
//            payType = "ZFFS_WX";
//        } else if (jsonObj.get("source").toString().contains("_ZFB_")) {
//            payType = "ZFFS_ZFB";
//        } else if (jsonObj.get("source").toString().contains("_YL_")) {
//            payType = "ZFFS_YL";
//        }
////        支付来源
//        billListBean.setPayType(payType);
//        billListService.save(billListBean);
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer messageContainerC() {
//        //加载处理消息A的队列
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        //设置接收多个队列里面的消息，这里设置接收队列A
//        //假如想一个消费者处理多个队列里面的信息可以如下设置：
//        container.setQueues(queueD());
//        container.setExposeListenerChannel(true);
//        //设置最大的并发的消费者数量
//        container.setMaxConcurrentConsumers(10);
//        //最小的并发消费者的数量
//        container.setConcurrentConsumers(1);
//        //设置确认模式手工确认
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                /**通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，
//                 换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它 */
//                channel.basicQos(1);
//                byte[] body = message.getBody();
//                try {
//                    logger.info("接收处理队列配置信息当中的消息:" + new String(body, "UTF-8"));
//
//                    String request = new String(body, "UTF-8");
//                    JSONObject requestTemp = JSONObject.fromObject(request);
//                    setGetEurekaInfo(requestTemp);
//                    /**为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
//                     当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    /**为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
//                     当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
//                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                }
//
//            }
//        });
//        return container;
//    }
//
//    /**
//     * 把服务器信息写入到redis里面去
//     *
//     * @param
//     */
//    public void setGetEurekaInfo(JSONObject jsonObject) {
//        Map newMap = new HashMap();
//        JSONObject json= JSONObject.fromObject(jsonObject.get("projectContent"));
//        for (Object str : json.keySet()) {
//            newMap.put(str, json.get(str).toString());
//        }
//        redisTemplate.opsForHash().putAll(InterfaceConstants.REIDIS_EUREKAINFO_KEY + "-" +jsonObject.get("projectNumber"), newMap);
//    }
//}
