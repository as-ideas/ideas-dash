# only deploy a new version to the maven repo from the master branch
if [ "$TRAVIS_BRANCH" == "master" ]; then
    goal=deploy
else
    goal=install
fi

mvn ${goal} --settings settings.xml
