
$(document).on("click", "#search_by_friend_remote_id", onSearchByFriendRemoteIdBtnClick);

function onSearchByFriendRemoteIdBtnClick() {
    $(location).attr('href', 'data-change-list?friend_remote_id=' + $('#friend_remote_id').val());
}
