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

# User stories

- DONE: I want to add my events (with their names and timestamps) into chrony.
- DONE: I want to be able to fetch a list of all events I added so far.

# Technical notes

- Chrony is a lightweight library (intended primarily for use within applications to optimise the application's user experience).
- Chrony's events are identified by the name.
- Chrony does not need any connection to the internet (no collected events ever get through the web).
- Chrony runs on smartphones and PCs.
- Chrony is reactive (whenever some events are added, the report should get updated).