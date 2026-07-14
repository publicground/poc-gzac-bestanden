
type OutputFormat = 'JSON' | 'Smart Documents XML' | 'Smart Documents JSON';

interface CustomValueLoggerActionConfig {
  templateData: Array<{key: string; value: string}>;
  outputFormat: OutputFormat;
}

export {CustomValueLoggerActionConfig, OutputFormat};
