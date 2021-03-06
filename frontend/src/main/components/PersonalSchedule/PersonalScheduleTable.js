import { hasRole } from 'main/utils/currentUser';
import React from 'react';
import OurTable, { ButtonColumn } from '../OurTable';
import { useBackendMutation } from 'main/utils/useBackend';
import {
  cellToAxiosParamsDeleteAdmin,
  cellToAxiosParamsDeleteUser,
  onDeleteSuccess,
} from 'main/utils/PersonalScheduleUtils';
import { useNavigate } from 'react-router-dom';

export default function PersonalScheduleTable({ schedules, currentUser }) {
  const navigate = useNavigate();

  const editCallback = (cell) => {
    // Stryker disable next-line all: localStorage set in test
    localStorage.setItem('temp', 0);
    navigate(`/personalschedule/edit/${cell.row.values.id}`);
  };

  // Stryker disable all : hard to test for query caching

  const deleteMutation = useBackendMutation(
    hasRole(currentUser, 'ROLE_ADMIN')
      ? cellToAxiosParamsDeleteAdmin
      : cellToAxiosParamsDeleteUser,
    { onSuccess: onDeleteSuccess },
    [
      hasRole(currentUser, 'ROLE_ADMIN')
        ? '/api/PersonalSchedules/admin/all'
        : '/api/PersonalSchedules/all',
    ]
  );
  // Stryker enable all

  // Stryker disable next-line all : TODO try to make a good test for this
  const deleteCallback = async (cell) => {
    deleteMutation.mutate(cell);
  };

  const columns = [
    {
      Header: 'id',
      accessor: 'id',
    },
    {
      Header: 'Name',
      accessor: 'name',
    },
    {
      Header: 'Description',
      accessor: 'description',
    },
    {
      Header: 'Quarter (YYYYQ)',
      accessor: 'quarterYYYYQ',
    },
  ];

  columns.push(
    ButtonColumn('Edit', 'primary', editCallback, 'PersonalScheduleTable')
  );
  columns.push(
    ButtonColumn('Delete', 'danger', deleteCallback, 'PersonalScheduleTable')
  );

  if (hasRole(currentUser, 'ROLE_ADMIN')) {
    columns.splice(1, 0, {
      Header: 'User',
      accessor: 'user.fullName',
    });
  }

  const memoizedColumns = React.useMemo(() => columns, [columns]);
  const memoizedSchedule = React.useMemo(() => schedules, [schedules]);

  return (
    <OurTable
      data={memoizedSchedule}
      columns={memoizedColumns}
      testid={'PersonalScheduleTable'}
    />
  );
}
