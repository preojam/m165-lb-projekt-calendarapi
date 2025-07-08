private void validateCron(String cronPattern) {
    try {
        cronParser.parse(cronPattern).validate();
    } catch (Exception e) {
        throw new InvalidCronException(cronPattern);
    }
}
