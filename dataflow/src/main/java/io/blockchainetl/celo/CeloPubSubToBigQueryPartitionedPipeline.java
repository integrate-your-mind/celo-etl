package io.blockchainetl.celo;

import com.google.api.services.bigquery.model.TableRow;
import io.blockchainetl.common.PubSubToBigQueryPipelineOptions;
import io.blockchainetl.common.domain.ChainConfig;
import io.blockchainetl.celo.fns.ConvertLogsToTableRowsFn;
import io.blockchainetl.celo.fns.ConvertTracesToTableRowsFn;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.blockchainetl.celo_partitioned.PubSubToBigQueryPartitionedPipeline.ENTITY_LOGS;
import static io.blockchainetl.celo_partitioned.PubSubToBigQueryPartitionedPipeline.ENTITY_TRACES;
import static io.blockchainetl.celo_partitioned.PubSubToBigQueryPartitionedPipeline.readChainConfigs;
import static io.blockchainetl.celo_partitioned.PubSubToBigQueryPartitionedPipeline.runPipeline;


public class CeloPubSubToBigQueryPartitionedPipeline {

    public static void main(String[] args) throws IOException, InterruptedException {
        PubSubToBigQueryPipelineOptions options =
            PipelineOptionsFactory.fromArgs(args).withValidation().as(PubSubToBigQueryPipelineOptions.class);

        runCeloPipeline(options);
    }

    static void runCeloPipeline(PubSubToBigQueryPipelineOptions options) {
        List<ChainConfig> chainConfigs = readChainConfigs(options.getChainConfigFile());

        Map<String, Class<? extends DoFn<String, TableRow>>> entityConfigs = new HashMap<>();
        entityConfigs.put(ENTITY_LOGS, ConvertLogsToTableRowsFn.class);
        entityConfigs.put(ENTITY_TRACES, ConvertTracesToTableRowsFn.class);
        runPipeline(options,chainConfigs, entityConfigs);
    }
}
