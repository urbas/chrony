# Things to do

- In the event detail screen:
  - I want to see the recurrence pattern of an event (e.g.: every Tuesday, every 2nd Sunday at 6pm, every Monday and Wednesday at 10am, etc.).
  - I want to see a graph of the event's value over time (a line graph will do).

## Bugs

- Events with no samples are placed at the bottom of the list. They should be placed on top, or into a special region in the list.

## Extracting recurrences

How to extract recurrences from a list of event samples?

### Clustering

We can cluster samples based on some interval. Such as daily, weekly, monthly, or yearly.

We want to detect typical human recurrences, such as:

- every d-th day at time t (starting on day p). This clusters on t.

- every w-th week on d-th day of week at time t. This clusters on t and d of week.

- every m-th month on d-th day of month at time t. This clusters on t and d of month.

- every y-th year on m-th month on d-th day of month at time t. This clusters t, d of month, and m of year.