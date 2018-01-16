
$(document).on("click", "#search_by_friend_remote_id", onSearchByFriendRemoteIdBtnClick);

function onSearchByFriendRemoteIdBtnClick() {
    $(location).attr('href', window.contextPath + 'data-change-list?' + window.baseUrlParams + '&friend_remote_id=' + $('#friend_remote_id').val());
}
