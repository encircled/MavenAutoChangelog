https://travis-ci.org/encircled/MavenAutoChangelog.svg?branch=master

# Changelog auto generator - maven plugin
MavenAutoChangelog is a simple maven plugin which generates changelog entries from git commit messages.

It can be configured to add only such messages which meet some format;

## Running
Maven goal `generate-changelog`. 

## Configuration

Maven plugin properties:
- *pathToChangelog* - Path to changelog file. Default is CHANGELOG.md
- *lastTag* - Predefined git tag which is used for comparing. Alternatively, `lastTagPattern` may be used to parse latest tag dynamically
- *lastTagPattern* - Regexp pattern which is used to match line with latest release version (tag). Alternatively, `lastTag` may be used if tag is predefined
- *lastTagFormat* - Defines format (for java `String.format(lastTagFormat, lastTagPattern)`) which is used to customize git tag if differs from `lastTagPattern`
- *applicableCommitPattern* - Regexp pattern which is used to filter unwanted commits, i.e. only commits which match this regexp will be included into changelog
- *commitFormat* - Additional java `String.format(commitMessage, commitFormat)` which can be used to customize changelog entry. Default is `%s`
- *unreleasedRowPattern* - Regexp pattern which should be used to match line with 'Unreleased' token
- *mergeRequestReplacePattern* - Regex pattern which is used to add merge request numbers to the messages. It must match the group which will be referenced in `mergeRequestReplacement`. For example `(])` to add merge requests before first `]` like `[ABC-123] Text  ==>  [ABC-123 321!] Text`.                                      
- *mergeRequestReplacement* - Replacement string which will be used with `mergeRequestReplacePattern`. Must contain token `MR#`, which will be replaced by merge request number. Default is " MR#$1"

### Sample
For example you have a CHANGELOG.md looking like
```html
[Unreleased]

[1.02] - 2017.01.02
- (FIX 20!) Critical bug fix
- (FEATURE 18!) Some cool feature
[1.01] - 2017.01.01
- (FEATURE 16!) Some cool feature
```

Your configuration may look like:
- *unreleasedRowPattern* - `.*[Unreleased]`
- *lastTagPattern* - `.*[\d.] - .*`
- *applicableCommitPattern* - `\(.*).*`
- *commitFormat* - `- %s`
- *mergeRequestReplacePattern* - `(\\))`
