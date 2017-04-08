package ru.friends.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Deprecated
public class VKFriendListManager extends TimerTask {
    @Override
    public void run() {
        throw new RuntimeException("Not implemented yet");
    }

    /*private static Logger log = Logger.getLogger(VKFriendListManager.class);
    private TreeSet<UserVo> users = new TreeSet<>(
            (e1,e2) -> e1 == null ? 0 : e2 == null ? 0 :
                    (int)(e1.getLastUpdate().getTimeInMillis() + e1.getInterval().getTimeInMillis() - e2.getLastUpdate().getTimeInMillis() - e2.getInterval().getTimeInMillis())
                    );

    @Autowired
    private FriendDAO friendsDAO;
    @Autowired
    private TransactionDAO transactionsDAO;
    @Autowired
    private UserDao usersDAO;

    public void addNewUser(UserVo user) throws IOException {
        user.setLastUpdate(Calendar.getInstance());
        update(user);
        this.users.add(user);
    }

    public void init() throws IOException {
        List<UserVo> users = usersDAO.getAll();
        for(UserVo user : users)
            if(user.getLastUpdate() == null)
                addNewUser(user);
            else
                this.users.add(user);
        new Timer().schedule(this, 0, 3000);

        TreeSet treeSet = new TreeSet();
        //reeSet.
    }

    //Возвращает результат множества a за вычетом множества b
    private List<FriendDto> getDifference(List<FriendDto> a, List<FriendDto> b) {
        List<FriendDto> difference = new ArrayList<>();
        difference.addAll(a);
        difference.removeAll(b);
        return difference;
    }

    private List<FriendDto> getFriendsFromDB(UserVo user) {
        return friendsDAO.getAddedFriendsByUserId(user.getId());
    }

    private List<FriendDto> getDeletedFriendsFromDB(UserVo user) {
        return friendsDAO.getDeletedFriendsByUserId(user.getId());
    }

    private List<FriendDto> getFriendsFromVK(UserVo user) throws IOException {
        return VKFriendsDAO.getResponseAsList("" + user.getId());
    }

    public void update(UserVo user) throws IOException {
        List<FriendDto> friendsFromDB = getFriendsFromDB(user);
        List<FriendDto> friendsFromVK = getFriendsFromVK(user);

        List<FriendDto> newFriends = getDifference(friendsFromVK, friendsFromDB);
        for(FriendDto friend : newFriends) {
            if(friendsDAO.getById(friend.getId()) == null)
                friendsDAO.save(friend);
            transactionsDAO.save(new TransactionDto(user.getLastUpdate(), Calendar.getInstance(), false, friend, user));
        }

        List<FriendDto> deletedFriends = getDifference(friendsFromDB, friendsFromVK);
        for(FriendDto friend : deletedFriends)
            transactionsDAO.save(new TransactionDto(user.getLastUpdate(), Calendar.getInstance(), true, friend, user));

        user.setLastUpdate(Calendar.getInstance());
    }

    *//*
    public List<FriendDto> getAddedFriendsByUserId(UserVo user) {
        return getFriendsFromDB(user);
    }

    public List<FriendDto> getDeletedFriendsByUserId(UserVo user) {
        return getDeletedFriendsFromDB(user);
    }
    *//*

    @Override
    public void run() {
        try {
            if(users.last().getLastUpdate().getTimeInMillis() + users.last().getInterval().getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                update(users.last());
            System.out.println();
            users.forEach(e -> System.out.print(e.getId() + "(" + e.getLastUpdate().MINUTE + ":" + e.getLastUpdate().SECOND + ") "));
        } catch (IOException ex) {
            log.fatal("VKFriendListManager: can't update user", ex);
        }
    }*/

}
