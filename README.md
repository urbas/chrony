# Chrony

Chrony takes as input events from your personal life and produces a _personal report_ of how __relevant__ the collected events are at the present, how __frequent__ they are, how __regular__ they are, and whether they are part of a __ceremony__.

Let's define the above terms.

## Relevance

Relevance is a measure of how high on the personal report an event should be listed.

## Event frequency

The frequency of an event is a measure of how often they repeated within a certain time span.

## Regularity

The regularity of an event is a measure of how closely they align with other cycles in a typical human life. An event can have a daily regularity, it may happens every day in the morning.

## Ceremony

A ceremony is a sequence of events that regularly or frequently occur together in a specific order.

# Use cases

- I want to see the most relevant events (in descending order).
- I want to see the frequency of listed events (in terms of repetitions in an hour, day, week, month, etc.).
- I want the most frequent events to rank high on relevance.

# Technical notes

- Chrony should not need any connection to the internet (no collected events should ever get through the web).
- Chrony should be reactive (whenever some events are added, the report should get updated).
- Chrony should run on smartphones and PCs.