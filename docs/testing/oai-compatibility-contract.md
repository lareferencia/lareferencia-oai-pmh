# OAI-PMH compatibility contract

This contract defines the behavior that the alignment work must preserve. It is
the checklist for regression fixtures and integration tests.

## Repository behavior

- `Identify` returns the configured repository name, base URL, administrator
  email, earliest datestamp, granularity and deleted-record policy.
- `ListMetadataFormats` lists every configured crosswalk and rejects unknown item
  identifiers according to OAI-PMH.
- `ListSets` exposes configured virtual sets plus Solr community and collection
  facets, with deterministic pagination.
- `ListIdentifiers` and `ListRecords` apply public-access filtering by default.
- `GetRecord` looks up the exact current identifier after safely escaping it for
  Solr.

## Identifier invariant

The public OAI identifier is the current repository handle. No `oai:` prefix or
other normalization is introduced by this migration. Given the handle
`20.500.12345/678`, all verbs continue to return and accept:

```text
20.500.12345/678
```

## Date behavior

- `from` is inclusive from the first millisecond represented by the request.
- `until` is inclusive through the last millisecond represented by the request.
- Date-only and date-time granularities are not mixed in one request.
- A dependency or runtime upgrade does not alter stored datestamps.

## Resumption tokens

- A token preserves metadata prefix, `from`, `until`, set and offset.
- Negative, non-numeric, malformed and out-of-range offsets are rejected.
- Following every token returns each matching record once, without gaps or
  duplicates.
- A token is not silently accepted with extra request arguments.

## Records and errors

- Private records are never returned by the default context.
- Deleted records follow the configured deleted-record policy.
- Missing optional Solr fields do not crash an otherwise valid harvest.
- Unknown identifiers and invalid arguments produce the corresponding OAI error.
- Solr connectivity or query failures are reported as service failures and are
  never represented as an empty successful harvest.

## Required test layers

1. Unit tests for filters, identifier lookup and token formatting.
2. Repository tests against a Solr Testcontainer using the canonical OAI core.
3. HTTP tests for every OAI verb and error response.
4. XML Schema validation for representative successful and error responses.
5. Differential tests comparing normalized responses from the old and migrated
   providers against the same read-only Solr dataset.
