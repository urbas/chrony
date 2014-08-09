# Chrony

Chrony takes as input events from your personal life and reports  how __frequent__ they are, how __regular__ they are,
whether they are part of a __ceremony__, or how __relevant__ they are at present.

Let's define the above terms.

## Event frequency

The frequency of an event is a measure of how often they repeated within a certain time span.

## Regularity

The regularity of an event is a measure of how closely they align with other cycles in a typical human life. An event
can have a daily regularity, it may happens every day in the morning.

## Ceremony

A ceremony is a sequence of events that regularly or frequently occur together in a specific order.

## Relevance

Relevance is a measure of how likely the event is to occur again (within a certain time frame).

# Technical notes

- Chrony should not need any connection to the internet (no collected events should ever get through the web).
- Chrony should be reactive (whenever some events are added, the report should get updated).
- Chrony should run on smartphones and PCs.