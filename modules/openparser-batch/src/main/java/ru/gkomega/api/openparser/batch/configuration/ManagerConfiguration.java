//package ru.gkomega.api.openparser.batch.configuration;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.IntegrationComponentScan;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.channel.QueueChannel;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.IntegrationFlows;
//
///**
// * This configuration class is for the manager side of the remote partitioning sample.
// * The manager step will create 3 partitions for workers to process.
// */
//@Configuration
//@IntegrationComponentScan
//public abstract class ManagerConfiguration {
//
//    private static final int GRID_SIZE = 3;
//
//    private final JobBuilderFactory jobBuilderFactory;
//
//    private final RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory;
//
//
//    public ManagerConfiguration(JobBuilderFactory jobBuilderFactory,
//                                RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory) {
//
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.managerStepBuilderFactory = managerStepBuilderFactory;
//    }
//
//    /*
//     * Configure outbound flow (requests going to workers)
//     */
//    @Bean
//    public DirectChannel requests() {
//        return new DirectChannel();
//    }
//
//    /*
//     * Configure inbound flow (replies coming from workers)
//     */
//    @Bean
//    public QueueChannel replies() {
//        return new QueueChannel();
//    }
//
//    //	@Bean
////	public IntegrationFlow inboundFlow(ActiveMQConnectionFactory connectionFactory) {
////		return IntegrationFlows
////				.from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("requests"))
////				.channel(requests())
////				.get();
////	}
//
//    @Bean
//    public IntegrationFlow integrationFlow() {
//        return IntegrationFlows
//                .from(requests())
//                //.handle(Jms.outboundAdapter(connectionFactory).destination("requests"))
//                .channel(replies())
//                .get();
//    }
//
//    //	@Bean
////	public IntegrationFlow outboundFlow(ActiveMQConnectionFactory connectionFactory) {
////		return IntegrationFlows
////				.from(replies())
////				.handle(Jms.outboundAdapter(connectionFactory).destination("replies"))
////				.get();
////	}
//
//    /*
//     * Configure the manager step
//     */
//    @Bean
//    public Step managerStep() {
//        return this.managerStepBuilderFactory.get("managerStep")
//                .partitioner("workerStep", new BasicPartitioner())
//                .gridSize(GRID_SIZE)
//                .<Integer, Integer>chunk(3)
//                .reader(itemReader())
//                .inputChannel(replies())
//                .outputChannel(requests())
//                .build();
//    }
//
//    //	@Bean
////	public IntegrationFlow workerIntegrationFlow() {
////		return this.remoteChunkingWorkerBuilder
////				.itemProcessor(itemProcessor())
////				.itemWriter(itemWriter())
////				.inputChannel(requests())
////				.outputChannel(replies())
////				.build();
////	}
//
//    @Bean
//    public Job remotePartitioningJob() {
//        return this.jobBuilderFactory.get("remotePartitioningJob")
//                .start(managerStep())
//                .build();
//    }
//}
