- I want to be able to add events with an associated numerical value.
- I want to see the frequency of an event (e.g.: 1/day, 1/week).
- I want to see the recurrence pattern of an event (e.g.: every Tuesday, every 2nd Sunday at 6pm, every Monday and Wednesday at 10am, etc.).
- I want to see a graph of the event's value over time (a line graph will do).

BUGS:
- Events with no samples produce wrong statistics (the minimum and maximum timestamps are Long.MIN_VALUE), which makes the sort algorithm go crazy, because relevance is a float and the precision is to low for such high differences between current events' and MIN_VALUE timestamps.