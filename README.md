# Changelog auto generator - maven plugin
MavenAutoChangelog is a simple maven plugin which generates changelog entries from git commit messages.

It can be configured to add only such messages which meet some format;

## Running
Maven goal `generate-changelog`. 

## Configuration

Maven plugin properties:
- *pathToChangelog* - Path to changelog file. Default is CHANGELOG.md
- *lastTagPattern* - Regexp pattern which should be used to match line with latest release version (tag)
- *lastTagFormat* - Format (for java <code>String.format(lastTagFormat, lastTagPattern)</code>) which can be used to customize git tag if differs from <code>lastTagPattern</code>. Default is %s
- *applicableCommitPattern* - Regexp pattern which is used to filter unwanted commits, i.e. only commits which match this regexp will be included into changelog
- *commitFormat* - Additional java <code>String.format(commitMessage, commitFormat)</code> which can be used to customize changelog entry. Default is %s
- *unreleasedRowPattern* - Regexp pattern which should be used to match line with 'Unreleased' token

### Sample
For example you have a CHANGELOG.md looking like
```html
[Unreleased]

[1.02] - 2017.01.02
- (FIX) Critical bug fix
- (FEATURE) Some cool feature
[1.01] - 2017.01.01
- (FEATURE) Some cool feature
```

Your configuration may look like:
```
- *unreleasedRowPattern* - .*[Unreleased]
- *lastTagPattern* - .*[\d.] - .*
- *applicableCommitPattern* - \(.*).* 
- *commitFormat* - - %s
```
