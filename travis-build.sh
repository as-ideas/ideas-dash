# only deploy a new version to the maven repo from the master branch
echo "building branch $TRAVIS_BRANCH (pullRequest: $TRAVIS_PULL_REQUEST)"

if [ "$TRAVIS_BRANCH" == "master" ]; then
    goal=deploy
else
    goal=install
fi

echo "mvn ${goal}"
mvn ${goal} --settings settings.xml
