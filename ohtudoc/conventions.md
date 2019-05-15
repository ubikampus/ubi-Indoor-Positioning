## Definition of done

* Tests have been implemented for the feature
* Pull request passes Travis CI
* Code review done in Github
* Feature branch has been merged to master

## Git conventions
All functionality is implemented in feature branches, which often references
single task from the backlog. When the code review is finished, the branch is
merged without fast forwarding to master branch. Force pushing is ok to feature
branches, but should not be done for master branch. Commit messages should be
descriptive and written with [idiomatic Git
conventions](https://chris.beams.io/posts/git-commit/#seven-rules).

## Code quality
All functionality should have automated tests, but the code coverage does not
need to be 100%.
