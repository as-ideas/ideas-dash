# only deploy a new version to the maven repo from the master branch.
# If the build was triggered from a pull request targeting the master,
# TRAVIS_BRANCH is "master" and TRAVIS_PULL_REQUEST is set to the pull request number.

echo "building branch $TRAVIS_BRANCH (pullRequest: $TRAVIS_PULL_REQUEST)"

if [ "$TRAVIS_BRANCH" == "master" && "$TRAVIS_PULL_REQUEST" == "false" ]; then
    goal=deploy
else
    goal=install
fi

echo "mvn ${goal}"
mvn ${goal} --settings settings.xml
